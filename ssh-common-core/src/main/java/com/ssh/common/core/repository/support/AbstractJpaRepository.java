package com.ssh.common.core.repository.support;

import com.ssh.common.core.entity.Entity;
import com.ssh.common.core.repository.JpaRepository;
import com.ssh.common.exception.DatabaseException;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of the {@link com.ssh.common.core.repository.JpaRepository} interface.
 * This will offer you a more sophisticated interface than the plain {@link EntityManager} .
 */
public abstract class AbstractJpaRepository implements JpaRepository {

    protected abstract EntityManager getEntityManager();

    @Override
    public <T extends Entity> T save(T entity) {
        if (entity.getId() == null) {
            getEntityManager().persist(entity);
            return entity;
        } else {
            return merge(entity);
        }
    }

    @Override
    public <T extends Entity> Iterable<T> save(Iterable<T> entities) {
        List<T> result = new ArrayList<>();
        if (entities == null) {
            return result;
        }
        for (T entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    @Override
    public <T extends Entity> T merge(T entity) {
        return getEntityManager().merge(entity);
    }

    @Override
    public <T extends Entity> void delete(T entity) {
        getEntityManager().remove(contains(entity) ? entity : merge(entity));
    }

    @Override
    public <T extends Entity> void delete(Iterable<T> entities) {
        for (T entity : entities) {
            delete(entity);
        }
    }

    @Override
    public <T extends Entity> void delete(Class<T> entityClass, Serializable primaryKey) {
        T entity = findById(entityClass, primaryKey);
        if (entity == null) {
            throw new DatabaseException(String.format("No %s entity with id %s exists!", entityClass.getSimpleName(), primaryKey));
        }
        delete(entity);
    }


    @Override
    public <T extends Entity> T findById(Class<T> entityClass, Serializable primaryKey) {
        return getEntityManager().find(entityClass, primaryKey);
    }

    @Override
    public <T extends Entity> T getById(Class<T> entityClass, Serializable primaryKey) {
        return getEntityManager().getReference(entityClass, primaryKey);
    }

    @Override
    public <T extends Entity> boolean contains(T entity) {
        return getEntityManager().contains(entity);
    }

    /**
     * flush(刷出)操作会马上执行SQL语句但不会清空Session缓存
     * 用于操作大批量数据以防止Session中对象过多而导致内存溢出
     */
    @Override
    public void flush() {
        getEntityManager().flush();
    }

    /**
     * clear(清空)操作会清空Session缓存但不会执行SQL语句
     * clear通常配合flush一起使用(先flush后clear)
     */
    @Override
    public void clear() {
        getEntityManager().clear();
    }

}
