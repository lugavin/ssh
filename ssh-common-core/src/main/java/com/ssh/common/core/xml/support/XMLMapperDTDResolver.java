package com.ssh.common.core.xml.support;

import com.ssh.common.core.xml.Mapper;
import com.ssh.common.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * DTD解析器
 */
public class XMLMapperDTDResolver implements EntityResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLMapperDTDResolver.class);

    private static final String PERIOD = Constant.PERIOD_SEPARATOR;
    private static final String SLASHES = Constant.SLASHES_SEPARATOR;
    private static final String DTD_FILE_PATH = Mapper.class.getPackage().getName().replace(PERIOD, SLASHES) + SLASHES;

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        LOGGER.debug("Trying to resolve XML entity with public ID [{}] and system ID [{}]", publicId, systemId);
        InputSource source = null;
        if (systemId != null && systemId.startsWith(Mapper.SYSTEM_ID_PREFIX)) {
            source = getInputSource(publicId, systemId, Mapper.SYSTEM_ID_PREFIX);
        }
        // return null triggers default behavior -> download from website or wherever.
        return source;
    }

    private InputSource getInputSource(String publicId, String systemId, String prefix) {
        InputSource source = null;
        String resource = DTD_FILE_PATH + systemId.substring(prefix.length());
        InputStream dtdStream = getInputStream(resource);
        if (dtdStream != null) {
            LOGGER.debug("Found mapper DTD [{}] in classpath:{}", systemId, resource);
            source = new InputSource(dtdStream);
            source.setPublicId(publicId);
            source.setSystemId(systemId);
        } else {
            LOGGER.warn("Could not resolve mapper DTD [{}]: not found in classpath:{}", systemId, resource);
        }
        return source;
    }

    protected InputStream getInputStream(String resource) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
    }

}
