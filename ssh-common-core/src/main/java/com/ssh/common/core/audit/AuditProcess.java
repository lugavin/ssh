package com.ssh.common.core.audit;

import com.ssh.common.core.entity.AuditColumnEntity;
import com.ssh.common.core.entity.AuditLogEntity;
import com.ssh.common.core.entity.AuditTableEntity;
import com.ssh.common.audit.AuditContext;
import com.ssh.common.util.JaxbMapper;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.action.BeforeTransactionCompletionProcess;
import org.hibernate.engine.SessionImplementor;

import java.util.Calendar;
import java.util.LinkedList;

public class AuditProcess implements BeforeTransactionCompletionProcess {

    private final SessionImplementor session;
    private final LinkedList<AuditEntry> auditEntries;

    public AuditProcess(SessionImplementor session) {
        this.session = session;
        this.auditEntries = new LinkedList<>();
    }

    public void add(AuditEntry auditEntry) {
        auditEntries.add(auditEntry);
    }

    private void remove(AuditEntry auditEntry) {
        auditEntries.remove(auditEntry);
    }

    private void execute(Session session) {
        AuditContext auditContext = AuditContext.getThreadInstance();
        AuditLogEntity auditLog = new AuditLogEntity(
                auditContext.getUserId(),
                auditContext.getFuncCode(),
                auditContext.getHostIP(),
                auditContext.getHostName(),
                auditContext.getRequestURL(),
                Calendar.getInstance().getTime(),
                null
        );
        session.save(auditLog);
        AuditEntry auditEntry;
        while ((auditEntry = auditEntries.poll()) != null) {
            AuditTableEntity auditTable = new AuditTableEntity(
                    auditEntry.getAction(),
                    auditEntry.getEntityId(),
                    auditEntry.getEntityName(),
                    auditEntry.getTableName(),
                    JaxbMapper.toXML(new AuditFieldWrapper(auditEntry.getAuditFields())).getBytes(),
                    auditLog
            );
            session.save(auditTable);
            for (AuditField auditField : auditEntry.getAuditFields()) {
                session.save(new AuditColumnEntity(
                        auditField.getPropertyName(),
                        auditField.getColumnName(),
                        auditField.getBeforeValue(),
                        auditField.getAfterValue(),
                        auditTable
                ));
            }
        }

        auditContext.setLogId(auditLog.getId());
    }

    @Override
    public void doBeforeTransactionCompletion(SessionImplementor session) {
        if (auditEntries.size() == 0) {
            return;
        }
        if (FlushMode.isManualFlushMode(session.getFlushMode())) {
            Session temporarySession = null;
            try {
                temporarySession = session.getFactory().openTemporarySession();
                execute(temporarySession);
                temporarySession.flush();
            } finally {
                if (temporarySession != null) {
                    temporarySession.close();
                }
            }
        } else {
            execute((Session) session);
            session.flush();
        }
    }

}
