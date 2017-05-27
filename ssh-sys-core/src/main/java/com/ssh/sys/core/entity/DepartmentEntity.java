package com.ssh.sys.core.entity;

import com.ssh.common.core.annotation.Audited;
import com.ssh.common.core.annotation.NotAudited;
import com.ssh.common.core.entity.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Audited
@Table(name = "SYS_DEPARTMENT")
public class DepartmentEntity extends AbstractEntity {

    private static final long serialVersionUID = 20160716L;

    private Long id;
    private String name;
    private DepartmentEntity parent;
    private Set<DepartmentEntity> children;
    private Set<UserEntity> users;

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

    @NotAudited
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true)
    @JoinColumn(name = "PARENT_ID")
    public DepartmentEntity getParent() {
        return parent;
    }

    public void setParent(DepartmentEntity parent) {
        this.parent = parent;
    }

    @NotAudited
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "parent")
    public Set<DepartmentEntity> getChildren() {
        return children;
    }

    public void setChildren(Set<DepartmentEntity> children) {
        this.children = children;
    }

    @NotAudited
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "department")
    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

}
