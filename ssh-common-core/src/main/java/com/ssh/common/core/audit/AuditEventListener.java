package com.ssh.common.core.audit;

import com.ssh.common.core.entity.Entity;
import com.ssh.common.core.enums.Action;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.*;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Audit Log Listener is used to log insert, update, delete, and load operations.
 * Complete list of listeners/events can be obtained at <tt>org.hibernate.event.EventListeners</tt>.
 */
public class AuditEventListener implements PostInsertEventListener, PostUpdateEventListener,
        PostDeleteEventListener, Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditEventListener.class);

    private AuditConfiguration auditConfiguration;

    /**
     * @see PostInsertEventListener#onPostInsert(PostInsertEvent)
     */
    @Override
    public void onPostInsert(PostInsertEvent event) {
        auditEvent(event.getSession(), event.getPersister(), Action.INSERT, event.getId(),
                event.getEntity().getClass(), null, event.getState());
    }

    /**
     * @see PostUpdateEventListener#onPostUpdate(PostUpdateEvent)
     */
    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        auditEvent(event.getSession(), event.getPersister(), Action.UPDATE, event.getId(),
                event.getEntity().getClass(), event.getOldState(), event.getState());
    }

    /**
     * @see PostDeleteEventListener#onPostDelete(PostDeleteEvent)
     */
    @Override
    public void onPostDelete(PostDeleteEvent event) {
        auditEvent(event.getSession(), event.getPersister(), Action.DELETE, event.getId(),
                event.getEntity().getClass(), event.getDeletedState(), null);
    }

    /**
     * @see Initializable#initialize(Configuration)
     */
    @Override
    public void initialize(Configuration configuration) {
        auditConfiguration = AuditConfiguration.get(configuration);
    }

    private void auditEvent(EventSource session, EntityPersister entityPersister, Action action,
                            Serializable entityId, Class<?> entityClass, Object[] oldState, Object[] newState) {
        if (AuditUtils.isAudited(entityClass)) {
            final AuditEntry auditEntry = createAuditEntry(action, entityId.toString(),
                    entityPersister.getEntityName(), AuditUtils.getTableName(entityClass));
            String[] propertyNames = entityPersister.getPropertyNames();
            if (!ObjectUtils.isEmpty(propertyNames)) {
                addAuditFields(session.getSessionFactory(), auditEntry, propertyNames, oldState, newState, entityClass);
            }
            auditConfiguration.getAuditProcessManager().get(session).add(auditEntry);
        }
    }

    private AuditEntry createAuditEntry(Action action, String entityId, String entityName, String tableName) {
        return new AuditEntry(action, entityId, entityName, tableName);
    }

    private void addAuditFields(SessionFactory sessionFactory, AuditEntry auditEntry, String[] propertyNames,
                                Object[] oldState, Object[] newState, Class<?> entityClass) {
        for (int i = 0; i < propertyNames.length; i++) {
            PropertyDescriptor descriptor = AuditUtils.getPropertyDescriptor(entityClass, propertyNames[i]);
            if (descriptor != null) {
                Method method = descriptor.getReadMethod();
                if (AuditUtils.isNotAudited(method) || AuditUtils.isCollection(method)) {
                    continue;
                }
                Object oldValue = (oldState != null) ? oldState[i] : null;
                Object newValue = (newState != null) ? newState[i] : null;
                String columnName = null;
                JoinColumn joinColumn = AuditUtils.getJoinColumn(method);
                if (joinColumn != null) {
                    if (oldValue instanceof Entity) {
                        oldValue = ((Entity) oldValue).getId();
                    }
                    if (newValue instanceof Entity) {
                        newValue = ((Entity) newValue).getId();
                    }
                    columnName = joinColumn.name();
                } else {
                    Column column = AuditUtils.getColumn(method);
                    if (column != null) {
                        columnName = column.name();
                    }
                }
                if (AuditUtils.isNotChanged(oldValue, newValue) || StringUtils.isEmpty(columnName)) {
                    continue;
                }
                auditEntry.addAuditField(new AuditField(
                        propertyNames[i],
                        columnName,
                        AuditUtils.getValue(oldValue),
                        AuditUtils.getValue(newValue)
                ));
            }
        }
    }

}
