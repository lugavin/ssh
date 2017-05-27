package com.ssh.common.core.repository.support;

import com.ssh.common.core.repository.template.StatementTemplate;
import com.ssh.common.core.repository.template.StatementTemplateBuilder;
import com.ssh.common.core.repository.DynamicQueryRepository;
import com.ssh.common.dto.DTO;
import com.ssh.common.page.Page;
import com.ssh.common.page.PageImpl;
import com.ssh.common.page.Pageable;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 当多线程访问公共数据时就有可能产生线程安全问题
 * 因此对于线程不安全的对象最好定义在方法体内作为局部变量使用而不建议定义为成员变量使用
 *
 * @author Gavin
 */
@Repository
public class SimpleDynamicQueryRepository extends AbstractJpaRepository implements DynamicQueryRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDynamicQueryRepository.class);

    private static final String COUNT_REPLACEMENT_TEMPLATE = "SELECT COUNT(*) %s";
    private static final Pattern ORDER_BY_PATTERN = Pattern.compile("\\s+ORDER\\s+BY\\s+.*", Pattern.CASE_INSENSITIVE);

    @Inject
    private StatementTemplateBuilder statementTemplateBuilder;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    //-------------------------------------------------------------------------
    // Convenient methods for storing individual objects
    //-------------------------------------------------------------------------

    /**
     * Execute the update or delete statement.
     *
     * @param statement The id of the statement to execute.
     * @param dto       Usually a Map<String, Object> or a JavaBean
     * @return The number of entities updated or deleted.
     */
    protected int executeUpdate(String statement, DTO dto) {
        return createQuery(statement, dto).executeUpdate();
    }

    //-------------------------------------------------------------------------
    // Convenient methods for loading single object
    //-------------------------------------------------------------------------

    /**
     * Query use the "?" placeholder (bind by order)
     */
    @Override
    public <T> T getOne(String statement, Object value) {
        return getOne(statement, value, null);
    }

    @Override
    public <T> T getOne(String statement, Object value, ResultTransformer transformer) {
        return getOne(statement, new Object[]{value}, transformer);
    }

    @Override
    public <T> T getOne(String statement, Object[] values) {
        return getOne(statement, values, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getOne(String statement, Object[] values, ResultTransformer transformer) {
        Query query = createQuery(statement);
        if (transformer != null) {
            query.setResultTransformer(transformer);
        }
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return (T) query.uniqueResult();
    }

    /**
     * Query use the "name" placeholder (bind by name)
     */
    @Override
    public <T> T getOne(String statement, String paramName, Object value) {
        return getOne(statement, new String[]{paramName}, new Object[]{value});
    }

    @Override
    public <T> T getOne(String statement, String[] paramNames, Object[] values) {
        return getOne(statement, paramNames, values, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getOne(String statement, String[] paramNames, Object[] values, ResultTransformer transformer) {
        if (paramNames.length != values.length) {
            throw new IllegalArgumentException("Length of paramNames array must match length of values array");
        }
        Query query = createQuery(statement);
        for (int i = 0; i < values.length; i++) {
            setParameter(query, paramNames[i], values[i]);
        }
        if (transformer != null) {
            query.setResultTransformer(transformer);
        }
        return (T) query.uniqueResult();
    }

    //-------------------------------------------------------------------------
    // Convenient methods for loading multiple objects
    //-------------------------------------------------------------------------

    @Override
    public <T> List<T> getList(String statement, DTO dto) {
        return getList(statement, dto, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String statement, DTO dto, ResultTransformer transformer) {
        Query query = createQuery(statement, dto);
        if (transformer != null) {
            query.setResultTransformer(transformer);
        }
        return query.list();
    }

    /**
     * Query use the "?" placeholder (bind by order)
     */
    @Override
    public <T> List<T> getList(String statement, Object value) {
        return getList(statement, value, null);
    }

    @Override
    public <T> List<T> getList(String statement, Object value, ResultTransformer transformer) {
        return getList(statement, new Object[]{value}, transformer);
    }

    @Override
    public <T> List<T> getList(String statement, Object[] values) {
        return getList(statement, values, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String statement, Object[] values, ResultTransformer transformer) {
        Query query = createQuery(statement);
        if (transformer != null) {
            query.setResultTransformer(transformer);
        }
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query.list();
    }

    /**
     * Query use the "name" placeholder (bind by name)
     */
    @Override
    public <T> List<T> getList(String statement, String paramName, Object value) {
        return getList(statement, paramName, value, null);
    }

    @Override
    public <T> List<T> getList(String statement, String paramName, Object value, ResultTransformer transformer) {
        return getList(statement, new String[]{paramName}, new Object[]{value}, transformer);
    }

    @Override
    public <T> List<T> getList(String statement, String[] paramNames, Object[] values) {
        return getList(statement, paramNames, values, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String statement, String[] paramNames, Object[] values, ResultTransformer transformer) {
        if (paramNames.length != values.length) {
            throw new IllegalArgumentException("Length of paramNames array must match length of values array");
        }
        Query query = createQuery(statement);
        for (int i = 0; i < values.length; i++) {
            setParameter(query, paramNames[i], values[i]);
        }
        if (transformer != null) {
            query.setResultTransformer(transformer);
        }
        return query.list();
    }

    //-------------------------------------------------------------------------
    // Convenient methods for pagination
    //-------------------------------------------------------------------------

    @Override
    public <T> Page<T> getPage(String statement, Pageable pageable) {
        return getPage(statement, pageable, null);
    }

    @Override
    public <T> Page<T> getPage(String statement, Pageable pageable, ResultTransformer transformer) {
        return getPage(statement, pageable.getParam(), pageable.getOffset(), pageable.getLimit(), transformer);
    }

    @SuppressWarnings("unchecked")
    private <T> Page<T> getPage(String statement, DTO dto, int offset, int limit, ResultTransformer transformer) {
        Query query = createQuery(statement, dto);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        if (transformer != null) {
            query.setResultTransformer(transformer);
        }
        List<T> list = query.list();
        int count = getCount(getStatementTemplate(statement).getType(), dto, query.getQueryString());
        return new PageImpl<>(offset / limit, limit, list, count);
    }

    //-------------------------------------------------------------------------
    // Helper methods used by the operations above
    //-------------------------------------------------------------------------

    /**
     * 不支持FreeMarker标签语法
     */
    protected int getCount(StatementTemplate.Type type, DTO dto, String queryString) {
        Query query = createQuery(String.format(COUNT_REPLACEMENT_TEMPLATE, removeSelect(removeOrderBy(queryString))), type);
        setProperties(query, dto);
        return ((Number) query.uniqueResult()).intValue();
    }

    /**
     * 支持FreeMarker标签语法
     */
    public int getCount(String statement, DTO dto) {
        StatementTemplate statementTemplate = getStatementTemplate(statement);
        String queryString = statementTemplateBuilder.processTemplate(statementTemplate, dto);
        return getCount(statementTemplate.getType(), dto, queryString);
    }

    /**
     * Remove select clause
     */
    private String removeSelect(String queryString) {
        return queryString.substring(queryString.toUpperCase(Locale.ENGLISH).indexOf("FROM"));
    }

    /**
     * Remove Order By clause
     */
    private String removeOrderBy(String queryString) {
        Matcher matcher = ORDER_BY_PATTERN.matcher(queryString);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    protected void setParameter(Query query, String paramName, Object value) {
        if (value instanceof Collection) {
            query.setParameterList(paramName, (Collection<?>) value);
        } else if (value instanceof Object[]) {
            query.setParameterList(paramName, (Object[]) value);
        } else {
            query.setParameter(paramName, value);
        }
    }

    protected void setProperties(Query query, DTO dto) {
        if (dto instanceof Map) {
            query.setProperties((Map) dto);
        } else {
            query.setProperties(dto);
        }
    }

    /**
     * 根据StatementId创建Query对象(不支持FreeMarker标签语法)
     *
     * @param statement Statement
     * @return Query对象
     */
    public Query createQuery(String statement) {
        StatementTemplate statementTemplate = getStatementTemplate(statement);
        String queryString = statementTemplate.getTemplate().toString();
        return createQuery(queryString, statementTemplate.getType());
    }

    /**
     * 根据StatementId和参数对象创建Query对象
     *
     * @param statement Statement
     * @param dto       Any JavaBean or POJO
     * @return Query对象
     */
    public Query createQuery(String statement, DTO dto) {
        StatementTemplate statementTemplate = getStatementTemplate(statement);
        String queryString = statementTemplateBuilder.processTemplate(statementTemplate, dto);
        Query query = createQuery(queryString, statementTemplate.getType());
        setProperties(query, dto);
        return query;
    }

    /**
     * 根据queryString和StatementTemplate.TYPE创建Query对象
     */
    public Query createQuery(String queryString, StatementTemplate.Type type) {
        return StatementTemplate.Type.HQL == type ? createHQLQuery(queryString) : createSQLQuery(queryString);
    }

    public Query createHQLQuery(String queryString) {
        return getSession().createQuery(queryString);
    }

    public SQLQuery createSQLQuery(String queryString) {
        return getSession().createSQLQuery(queryString);
    }

    /**
     * 获取Session对象
     */
    private Session getSession() {
        return getEntityManager().unwrap(Session.class);
    }

    private StatementTemplate getStatementTemplate(String statement) {
        StatementTemplate statementTemplate = statementTemplateBuilder.getStatementTemplate(statement);
        if (statementTemplate == null) {
            throw new IllegalArgumentException(String.format("Unknown query key: %s", statement));
        }
        return statementTemplate;
    }

}
