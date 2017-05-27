package com.ssh.common.core.repository;

import com.ssh.common.dto.DTO;
import com.ssh.common.page.Page;
import com.ssh.common.page.Pageable;
import org.hibernate.transform.ResultTransformer;

import java.util.List;

public interface DynamicQueryRepository extends JpaRepository {

    //-------------------------------------------------------------------------
    // Convenient methods for loading single object with placeholder
    // Note: This way does not support dynamic query
    //-------------------------------------------------------------------------

    /**
     * Query use the "?" placeholder (bind by order)
     */
    <T> T getOne(String statement, Object value);

    <T> T getOne(String statement, Object value, ResultTransformer transformer);

    <T> T getOne(String statement, Object[] values);

    <T> T getOne(String statement, Object[] values, ResultTransformer transformer);

    /**
     * Query use the "name" placeholder (bind by name)
     */
    <T> T getOne(String statement, String paramName, Object value);

    <T> T getOne(String statement, String[] paramNames, Object[] values);

    <T> T getOne(String statement, String[] paramNames, Object[] values, ResultTransformer transformer);

    //-------------------------------------------------------------------------
    // Convenient methods for loading multiple objects with placeholder
    // Note: This way does not support dynamic query
    //-------------------------------------------------------------------------

    /**
     * Query use the "?" placeholder (bind by order)
     */
    <T> List<T> getList(String statement, Object value);

    <T> List<T> getList(String statement, Object value, ResultTransformer transformer);

    <T> List<T> getList(String statement, Object[] values);

    <T> List<T> getList(String statement, Object[] values, ResultTransformer transformer);

    /**
     * Query use the "name" placeholder (bind by name)
     */
    <T> List<T> getList(String statement, String paramName, Object value);

    <T> List<T> getList(String statement, String paramName, Object value, ResultTransformer transformer);

    <T> List<T> getList(String statement, String[] paramNames, Object[] values);

    <T> List<T> getList(String statement, String[] paramNames, Object[] values, ResultTransformer transformer);

    //-------------------------------------------------------------------------
    // Convenient methods for loading multiple objects(support dynamic query)
    //-------------------------------------------------------------------------

    <T> List<T> getList(String statement, DTO dto);

    <T> List<T> getList(String statement, DTO dto, ResultTransformer transformer);

    //-------------------------------------------------------------------------
    // Convenient methods for pagination(support dynamic query)
    //-------------------------------------------------------------------------

    <T> Page<T> getPage(String statement, Pageable pageable);

    <T> Page<T> getPage(String statement, Pageable pageable, ResultTransformer transformer);

}
