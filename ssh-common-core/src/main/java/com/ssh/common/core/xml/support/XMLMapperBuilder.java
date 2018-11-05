package com.ssh.common.core.xml.support;

import com.ssh.common.core.xml.MapperBuilder;
import com.ssh.common.util.Constant;
import com.ssh.common.util.IOUtils;
import org.apache.commons.lang3.Validate;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.xml.sax.EntityResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 动态 HQL/SQL 组装器
 */
public class XMLMapperBuilder implements MapperBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLMapperBuilder.class);

    private EntityResolver entityResolver = new XMLMapperDTDResolver();

    private Resource[] locations;

    private Map<String, String> hqlMap;

    private Map<String, String> sqlMap;

    private Set<String> nameCache;

    public void setLocations(Resource... locations) {
        this.locations = locations;
    }

    @Override
    public void init() throws Exception {
        hqlMap = new HashMap<>();
        sqlMap = new HashMap<>();
        nameCache = new HashSet<>();
        buildMap(locations);
        nameCache.clear();
    }

    @Override
    public Map<String, String> getHqlMap() {
        return hqlMap;
    }

    @Override
    public Map<String, String> getSqlMap() {
        return sqlMap;
    }

    private void buildMap(Resource[] resources) throws Exception {
        if (resources != null && resources.length > 0) {
            for (Resource resource : resources) {
                LOGGER.debug("Trying to load resource [{}]", resource);
                buildMap(resource);
                LOGGER.debug("Successfully loaded resource [{}]", resource.getFilename());
            }
        }
    }

    private void buildMap(Resource resource) throws Exception {
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            SAXReader saxReader = new SAXReader();
            saxReader.setEntityResolver(entityResolver);
            saxReader.setMergeAdjacentText(true);
            saxReader.setValidation(true);
            Document document = saxReader.read(inputStream);
            Element rootElement = document.getRootElement();
            if (ROOT_ELEMENT.equals(rootElement.getName())) {
                Iterator iterator = rootElement.elementIterator();
                while (iterator.hasNext()) {
                    final Element element = (Element) iterator.next();
                    String nodeName = element.getName();
                    if (HQL_ELEMENT.equals(nodeName)) {
                        putStatementToCache(rootElement, element, hqlMap, resource);
                    } else if (SQL_ELEMENT.equals(nodeName)) {
                        putStatementToCache(rootElement, element, sqlMap, resource);
                    } else {
                        throw new IllegalArgumentException(String.format("Unknown tag name: %s", nodeName));
                    }
                }
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private void putStatementToCache(final Element rootElement, final Element element, Map<String, String> cacheMap, Resource resource) throws IOException {
        String namespace = Validate.notEmpty(rootElement.attribute(NAMESPACE_ATTRIBUTE).getText());
        String statementId = Validate.notEmpty(element.attribute(ID_ATTRIBUTE).getText());
        String queryKey = applyNamespace(namespace, statementId);
        if (nameCache.contains(queryKey)) {
            throw new RuntimeException(String.format("Statement[namespace=%s,id=%s] has been defined in [%s].", namespace, statementId, resource.getFilename()));
        }
        nameCache.add(queryKey);
        cacheMap.put(queryKey, element.getText());
    }

    private String applyNamespace(String namespace, String statementId) {
        return namespace + Constant.PERIOD_SEPARATOR + statementId;
    }

}
