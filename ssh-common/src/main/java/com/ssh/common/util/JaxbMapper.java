package com.ssh.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class JaxbMapper {

    private static final ConcurrentMap<Class, JAXBContext> MAP = new ConcurrentHashMap<>();

    public static String toXML(Object root) {
        return toXML(root, root.getClass(), null);
    }

    public static String toXML(Object root, String encoding) {
        return toXML(root, root.getClass(), encoding);
    }

    public static String toXML(Object root, Class clazz, String encoding) {
        try {
            StringWriter writer = new StringWriter();
            createMarshaller(clazz, encoding).marshal(root, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXML(String xml, Class<T> clazz) {
        try {
            return (T) createUnmarshaller(clazz).unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static Marshaller createMarshaller(Class clazz, String encoding) {
        try {
            Marshaller marshaller = getJaxbContext(clazz).createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            if (StringUtils.isNotBlank(encoding)) {
                marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            }
            return marshaller;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static Unmarshaller createUnmarshaller(Class clazz) {
        try {
            return getJaxbContext(clazz).createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    protected static JAXBContext getJaxbContext(Class clazz) {
        JAXBContext jaxbContext = MAP.get(clazz);
        if (jaxbContext == null) {
            try {
                jaxbContext = JAXBContext.newInstance(clazz);
                MAP.putIfAbsent(clazz, jaxbContext);
            } catch (JAXBException e) {
                throw new RuntimeException("Could not instantiate JAXBContext for class [" + clazz + "]: " + e.getMessage(), e);
            }
        }
        return jaxbContext;
    }

}
