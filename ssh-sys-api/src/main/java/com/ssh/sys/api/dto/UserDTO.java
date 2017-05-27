package com.ssh.sys.api.dto;

import com.ssh.common.dto.DTO;
import com.ssh.common.validation.Groups;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserDTO implements DTO {

    private static final long serialVersionUID = 20160610L;

    @NotNull(groups = {Groups.Update.class}, message = "{user.id.null}")
    @Min(groups = {Groups.Update.class}, message = "{user.id.invalid}", value = 1)
    private Long id;

    @NotBlank(groups = {Groups.Add.class}, message = "{user.code.empty}")
    @Pattern(groups = {Groups.Add.class}, message = "{user.code.pattern.invalid}", regexp = "^[A-Za-z][A-Za-z0-9]{4,10}$")
    private String code;

    private String name;

    private String pass;

    private String salt;

    private Integer status;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
