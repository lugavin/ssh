package com.ssh.sys.core.entity;

import com.ssh.common.core.annotation.Audited;
import com.ssh.common.core.annotation.NotAudited;
import com.ssh.common.core.entity.AbstractEntity;
import com.ssh.sys.core.enums.ResourceType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Audited
@Table(name = "SYS_PERMISSION")
public class PermissionEntity extends AbstractEntity {

    private static final long serialVersionUID = 20160528L;

    private Long id;
    private String name;
    private String url;
    private String code;
    private ResourceType type;
    private String icon;
    private Integer status;
    private String parentIds;
    private Integer seq;
    private PermissionEntity parent;
    private Set<PermissionEntity> children;
    private Set<RoleEntity> roles;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "NAME", nullable = false, length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "URL", length = 128)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "CODE", length = 128)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "TYPE", length = 10)
    @Enumerated(EnumType.STRING)
    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    @Column(name = "ICON", length = 128)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "PARENT_IDS", length = 128)
    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    @Column(name = "SEQ")
    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @NotAudited
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true)
    @JoinColumn(name = "PARENT_ID")
    public PermissionEntity getParent() {
        return parent;
    }

    public void setParent(PermissionEntity parent) {
        this.parent = parent;
    }

    @NotAudited
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "parent")
    public Set<PermissionEntity> getChildren() {
        return children;
    }

    public void setChildren(Set<PermissionEntity> children) {
        this.children = children;
    }

    @NotAudited
    @ManyToMany
    @JoinTable(name = "SYS_PERMISSION_ROLE",
            joinColumns = @JoinColumn(name = "PERMISSION_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

}
