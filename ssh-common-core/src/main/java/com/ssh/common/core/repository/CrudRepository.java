package com.ssh.common.core.repository;

import com.ssh.common.core.entity.Entity;

import java.io.Serializable;

/**
 * @see com.ssh.common.core.repository.JpaRepository
 * @see com.ssh.common.core.repository.support.AbstractJpaRepository
 */
public interface CrudRepository<T extends Entity, ID extends Serializable> {

    T save(T entity);

    Iterable<T> save(Iterable<T> entities);

    T merge(T entity);

    void delete(T entity);

    void delete(Iterable<T> entities);

    void delete(ID primaryKey);

    T findById(ID primaryKey);

    T getById(ID primaryKey);

    boolean contains(T entity);

    void flush();

    void clear();

}
