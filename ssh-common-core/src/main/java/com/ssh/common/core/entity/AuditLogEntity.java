package com.ssh.common.core.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "SYS_AUDIT_LOG")
public class AuditLogEntity extends AbstractEntity {

    private static final long serialVersionUID = 20160717L;

    private Long id;
    private Long actorId;
    private String funcCode;
    private String hostIP;
    private String hostName;
    private String requestURL;
    private Date actionTime;
    private String remark;

    private Set<AuditTableEntity> auditTables;

    public AuditLogEntity() {
    }

    public AuditLogEntity(Long actorId, String funcCode, String hostIP, String hostName,
                          String requestURL, Date actionTime, String remark) {
        this.actorId = actorId;
        this.funcCode = funcCode;
        this.hostIP = hostIP;
        this.hostName = hostName;
        this.requestURL = requestURL;
        this.actionTime = actionTime;
        this.remark = remark;
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

    @Column(name = "ACTOR_ID")
    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    @Column(name = "FUNC_CODE")
    public String getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    @Column(name = "HOST_IP")
    public String getHostIP() {
        return hostIP;
    }

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    @Column(name = "HOST_NAME")
    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Column(name = "REQUEST_URL")
    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    @Column(name = "ACTION_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "auditLog")
    public Set<AuditTableEntity> getAuditTables() {
        return auditTables;
    }

    public void setAuditTables(Set<AuditTableEntity> auditTables) {
        this.auditTables = auditTables;
    }

}
