package com.ssh.common.core.audit;

import org.hibernate.Transaction;
import org.hibernate.action.AfterTransactionCompletionProcess;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.event.EventSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuditProcessManager {

    private final Map<Transaction, AuditProcess> auditProcessMap;

    public AuditProcessManager() {
        auditProcessMap = new ConcurrentHashMap<>();
    }

    public AuditProcess get(EventSource session) {
        final Transaction transaction = session.getTransaction();
        AuditProcess auditProcess = auditProcessMap.get(transaction);
        if (auditProcess == null) {
            // No worries about registering a transaction twice - a transaction is single thread
            auditProcess = new AuditProcess(session);
            auditProcessMap.put(transaction, auditProcess);
            // Register beforeTransactionCompletionProcess
            session.getActionQueue().registerProcess(auditProcess);
            // Register afterTransactionCompletionProcess
            session.getActionQueue().registerProcess(new AfterTransactionCompletionProcess() {
                public void doAfterTransactionCompletion(boolean success, SessionImplementor session) {
                    auditProcessMap.remove(transaction);
                }
            });
        }
        return auditProcess;
    }

}
