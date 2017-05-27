package com.ssh.common.core.audit;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

@XmlRootElement
public class AuditField implements Serializable {

    private static final long serialVersionUID = 20160727L;

    private Long id;
    private String propertyName;
    private String columnName;
    private String beforeValue;
    private String afterValue;

    // private AuditEntry auditEntry;

    public AuditField() {
    }

    public AuditField(String propertyName, String columnName,
                      String beforeValue, String afterValue) {
        this.propertyName = propertyName;
        this.columnName = columnName;
        this.beforeValue = beforeValue;
        this.afterValue = afterValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @XmlElement
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @XmlElement(nillable = true)
    public String getBeforeValue() {
        return beforeValue;
    }

    public void setBeforeValue(String beforeValue) {
        this.beforeValue = beforeValue;
    }

    @XmlElement(nillable = true)
    public String getAfterValue() {
        return afterValue;
    }

    public void setAfterValue(String afterValue) {
        this.afterValue = afterValue;
    }

    // public AuditEntry getAuditEntry() {
    //     return auditEntry;
    // }
    //
    // public void setAuditEntry(AuditEntry auditEntry) {
    //     this.auditEntry = auditEntry;
    // }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
