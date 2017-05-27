package com.ssh.sys.api.dto;

import com.ssh.common.dto.DTO;
import com.ssh.common.validation.Groups;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PermissionDTO implements DTO {

    private static final long serialVersionUID = 20161204L;

    @NotNull(groups = {Groups.Update.class})
    @Min(groups = {Groups.Update.class}, value = 1)
    private Long id;

    // @NotBlank(groups = {Groups.Add.class})
    // @Size(groups = {Groups.Add.class}, min = 4, max = 50)
    private String code;

    @NotBlank(groups = {Groups.Add.class, Groups.Update.class})
    private String name;

    @NotBlank(groups = {Groups.Add.class, Groups.Update.class})
    private String type;

    // @NotBlank(groups = {PermissionService.Add.class, PermissionService.Update.class})
    private String url;

    @NotNull(groups = {Groups.Update.class})
    @Min(groups = {Groups.Update.class}, value = 1)
    @Max(groups = {Groups.Update.class}, value = 99999)
    private Integer seq;

    private Long parentId;

    private String parentIds;

    private Integer status;

    private String icon;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
