package com.ssh.common.core.entity;

import com.ssh.common.core.enums.Action;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "SYS_AUDIT_TABLE")
public class AuditTableEntity extends AbstractEntity {

    private static final long serialVersionUID = 20160819L;

    private Long id;
    private Action action;
    private String entityId;
    private String entityName;
    private String tableName;
    private byte[] tableColumns;

    private AuditLogEntity auditLog;
    private Set<AuditColumnEntity> auditColumns;

    public AuditTableEntity() {
    }

    public AuditTableEntity(Action action, String entityId, String entityName, String tableName) {
        this.action = action;
        this.entityId = entityId;
        this.entityName = entityName;
        this.tableName = tableName;
    }

    public AuditTableEntity(Action action, String entityId, String entityName, String tableName,
                            byte[] tableColumns) {
        this(action, entityId, entityName, tableName);
        this.tableColumns = tableColumns;
    }

    public AuditTableEntity(Action action, String entityId, String entityName, String tableName,
                            byte[] tableColumns, AuditLogEntity auditLog) {
        this(action, entityId, entityName, tableName, tableColumns);
        this.auditLog = auditLog;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "ACTION")
    @Enumerated(EnumType.STRING)
    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Column(name = "ROW_ID")
    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @Column(name = "ENTITY_NAME")
    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Column(name = "TABLE_NAME")
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Lob
    @Column(name = "TABLE_COLUMNS")
    public byte[] getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(byte[] tableColumns) {
        this.tableColumns = tableColumns;
    }


    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "AUDIT_LOG_ID")
    public AuditLogEntity getAuditLog() {
        return auditLog;
    }

    public void setAuditLog(AuditLogEntity auditLog) {
        this.auditLog = auditLog;
    }

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "auditTable")
    public Set<AuditColumnEntity> getAuditColumns() {
        return auditColumns;
    }

    public void setAuditColumns(Set<AuditColumnEntity> auditColumns) {
        this.auditColumns = auditColumns;
    }

}
