package com.ssh.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * JSON String <-> Java Object
 */
public class JsonMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonMapper.class);

    private ObjectMapper mapper = new ObjectMapper();

    public JsonMapper(Include include) {
        // 设置输出时包含属性
        mapper.setSerializationInclusion(include);
        // 设置输入时忽略在JSON字符串中存在而Java对象没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static JsonMapper buildNormalMapper() {
        return new JsonMapper(Include.ALWAYS);
    }

    public static JsonMapper buildNonNullMapper() {
        return new JsonMapper(Include.NON_NULL);
    }

    /**
     * 创建只输出非空属性到JSON字符串的Mapper
     */
    public static JsonMapper buildNonEmptyMapper() {
        return new JsonMapper(Include.NON_EMPTY);
    }

    /**
     * 创建只输出初始值被修改的属性到JSON字符串的Mapper
     */
    public static JsonMapper buildNonDefaultMapper() {
        return new JsonMapper(Include.NON_DEFAULT);
    }

    public String serialize(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            LOGGER.warn("Write to JSON string error: {}", e);
            return null;
        }
    }

    public String serialize(String function, Object object) {
        return serialize(new JSONPObject(function, object));
    }

    public <T> T deserialize(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.warn("Parse JSON string {} error: {}", json, e);
            return null;
        }
    }

    public <T> T deserialize(String json, JavaType javaType) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            LOGGER.warn("Parse JSON string {} error: {}", json, e);
            return null;
        }
    }

    public JavaType contructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    public JavaType contructMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        return mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
    }

    /**
     * 当JSON中只含有Bean的部分属性时, 更新一个已存在的Bean, 只覆盖该部分属性
     */
    public void update(String jsonString, Object object) {
        try {
            mapper.readerForUpdating(object).readValue(jsonString);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Update JSON string: {} to object: {} error.", jsonString, object);
        } catch (IOException e) {
            LOGGER.warn("Update JSON string: {} to object: {} error: ", jsonString, object, e);
        }
    }

    /**
     * 設定是否使用Enum的toString函數來讀寫Enum.
     * 為False時使用Enum的name()函數來讀寫Enum(默認為False).
     * 注意本函數一定要在Mapper創建後所有的讀寫動作之前調用.
     */
    public void enableEnumUseToString() {
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }

    /**
     * 支持使用Jaxb的Annotation, 使得POJO上的annotation不用与Jackson耦合.
     * 默认会先查找jaxb的annotation, 如果找不到再找jackson的.
     */
    public void enableJaxbAnnotation() {
        mapper.registerModule(new JaxbAnnotationModule());
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
