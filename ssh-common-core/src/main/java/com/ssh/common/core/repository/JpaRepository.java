package com.ssh.common.core.repository;

import com.ssh.common.core.entity.Entity;

import java.io.Serializable;

/**
 * JPA specific extension of {@link com.ssh.common.core.repository.Repository}
 */
public interface JpaRepository extends Repository {

    <T extends Entity> T save(T entity);

    <T extends Entity> Iterable<T> save(Iterable<T> entities);

    <T extends Entity> T merge(T entity);

    <T extends Entity> void delete(T entity);

    <T extends Entity> void delete(Iterable<T> entities);

    <T extends Entity> void delete(Class<T> entityClass, Serializable primaryKey);

    <T extends Entity> T findById(Class<T> entityClass, Serializable primaryKey);

    <T extends Entity> T getById(Class<T> entityClass, Serializable primaryKey);

    <T extends Entity> boolean contains(T entity);

    void flush();

    void clear();

}
