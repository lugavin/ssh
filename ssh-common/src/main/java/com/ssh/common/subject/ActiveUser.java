package com.ssh.common.subject;

import com.ssh.common.dto.DTO;

import java.util.List;

public class ActiveUser implements DTO {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String name;
    private String pass;
    private String salt;
    private Integer status;
    private List<Permission> menus;
    private List<Permission> functions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Permission> getMenus() {
        return menus;
    }

    public void setMenus(List<Permission> menus) {
        this.menus = menus;
    }

    public List<Permission> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Permission> functions) {
        this.functions = functions;
    }

    @Override
    public String toString() {
        return "ActiveUser [id=" + id + ", code=" + code
                + ", name=" + name + "]";
    }

}
