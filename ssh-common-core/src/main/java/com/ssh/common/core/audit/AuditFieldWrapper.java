package com.ssh.common.core.audit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "auditFields")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuditFieldWrapper {

    @XmlElement(name = "auditField")
    private List<AuditField> auditFields;

    public AuditFieldWrapper() {
    }

    public AuditFieldWrapper(List<AuditField> auditFields) {
        this.auditFields = auditFields;
    }

    public List<AuditField> getAuditFields() {
        return auditFields;
    }

    public void setAuditFields(List<AuditField> auditFields) {
        this.auditFields = auditFields;
    }
}
