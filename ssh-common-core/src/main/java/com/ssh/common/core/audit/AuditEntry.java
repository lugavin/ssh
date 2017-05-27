package com.ssh.common.core.audit;

import com.ssh.common.core.enums.Action;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuditEntry implements Serializable {

    private static final long serialVersionUID = 20160725L;

    private Long id;
    private Action action;
    private String entityId;
    private String entityName;
    private String tableName;

    private List<AuditField> auditFields;

    public AuditEntry() {
    }

    public AuditEntry(Action action, String entityId, String entityName, String tableName) {
        this.action = action;
        this.entityId = entityId;
        this.entityName = entityName;
        this.tableName = tableName;
    }

    public AuditEntry(Action action, String entityId, String entityName,
                      String tableName, List<AuditField> auditFields) {
        this(action, entityId, entityName, tableName);
        this.auditFields = auditFields;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<AuditField> getAuditFields() {
        if (this.auditFields == null) {
            this.auditFields = new ArrayList<>();
        }
        return auditFields;
    }

    public void setAuditFields(List<AuditField> auditFields) {
        this.auditFields = auditFields;
    }

    public void addAuditField(AuditField auditField) {
        if (auditField == null) {
            return;
        }
        getAuditFields().add(auditField);
        // auditField.setAuditEntry(this);
    }

    public void removeAuditField(AuditField auditField) {
        if (auditField == null) {
            return;
        }
        getAuditFields().remove(auditField);
        // auditField.setAuditEntry(null);
    }

    public void removeAuditFields() {
        Set<AuditField> auditFieldSet = new HashSet<>();
        auditFieldSet.addAll(getAuditFields());
        for (AuditField auditField : auditFieldSet) {
            removeAuditField(auditField);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
