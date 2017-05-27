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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Audited
@Table(name = "SYS_USER")
public class UserEntity extends AbstractEntity {

    private static final long serialVersionUID = 20160521L;

    private Long id;
    private String code;
    private String name;
    private String pass;
    private String salt;
    private Integer status;
    private Set<RoleEntity> roles;
    private DepartmentEntity department;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @SequenceGenerator(name = "seq", sequenceName = "SYS_USER_SEQ", allocationSize = 1)
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "CODE", nullable = false, length = 20, unique = true)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "NAME", nullable = false, length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotAudited
    @Column(name = "PASS", nullable = false, length = 32)
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @NotAudited
    @Column(name = "SALT", length = 32)
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true)
    @JoinColumn(name = "DEPARTMENT_ID")
    public DepartmentEntity getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }

}
