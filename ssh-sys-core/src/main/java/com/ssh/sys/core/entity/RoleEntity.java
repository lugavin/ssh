package com.ssh.sys.core.entity;

import com.ssh.common.core.annotation.Audited;
import com.ssh.common.core.annotation.NotAudited;
import com.ssh.common.core.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Audited
@Table(name = "SYS_ROLE")
public class RoleEntity extends AbstractEntity {

    private static final long serialVersionUID = 20160528L;

    private Long id;
    private String name;
    private String remark;
    private Integer status;
    private Set<UserEntity> users;
    private Set<PermissionEntity> permissions;

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

    @Column(name = "REMARK", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @NotAudited
    @ManyToMany
    @JoinTable(name = "SYS_USER_ROLE",
            joinColumns = @JoinColumn(name = "ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID")
    )
    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    @NotAudited
    @ManyToMany
    @JoinTable(name = "SYS_PERMISSION_ROLE",
            joinColumns = @JoinColumn(name = "ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID")
    )
    public Set<PermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionEntity> permissions) {
        this.permissions = permissions;
    }

}
