package com.ssh.common.core.entity;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "SYS_AUDIT_COLUMN")
public class AuditColumnEntity extends AbstractEntity {

    private static final long serialVersionUID = 20160717L;

    private Long id;
    private String propertyName;
    private String columnName;
    private String beforeValue;
    private String afterValue;

    private AuditTableEntity auditTable;

    public AuditColumnEntity() {
    }

    public AuditColumnEntity(String propertyName, String columnName, String beforeValue, String afterValue) {
        this.propertyName = propertyName;
        this.columnName = columnName;
        this.beforeValue = beforeValue;
        this.afterValue = afterValue;
    }

    public AuditColumnEntity(String propertyName, String columnName, String beforeValue, String afterValue,
                             AuditTableEntity auditTable) {
        this(propertyName, columnName, beforeValue, afterValue);
        this.auditTable = auditTable;
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

    @Column(name = "COLUMN_NAME")
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Column(name = "PROPERTY_NAME")
    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Column(name = "BEFORE_VALUE")
    public String getBeforeValue() {
        return beforeValue;
    }

    public void setBeforeValue(String beforeValue) {
        this.beforeValue = beforeValue;
    }

    @Column(name = "AFTER_VALUE")
    public String getAfterValue() {
        return afterValue;
    }

    public void setAfterValue(String afterValue) {
        this.afterValue = afterValue;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "AUDIT_TABLE_ID")
    public AuditTableEntity getAuditTable() {
        return auditTable;
    }

    public void setAuditTable(AuditTableEntity auditTable) {
        this.auditTable = auditTable;
    }

}
