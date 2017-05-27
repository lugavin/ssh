package com.ssh.common.core.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractEntity implements Entity {

    private static final long serialVersionUID = 20160528L;

    protected Serializable id;

    public void setId(Serializable id) {
        this.id = id;
    }

    @Override
    @Transient
    public Serializable getId() {
        return id;
    }

}
