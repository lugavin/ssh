package com.ssh.common.core;

import com.ssh.common.core.audit.AuditField;
import com.ssh.common.core.audit.AuditFieldWrapper;
import com.ssh.common.util.JaxbMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JaxbTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JaxbTest.class);

    @Test
    public void testJaxb() throws Exception {

        List<AuditField> auditFieldList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AuditField auditField = new AuditField();
            auditField.setPropertyName("name_" + i);
            auditField.setColumnName("name_" + i);
            auditField.setBeforeValue(Integer.toString(i));
            auditField.setAfterValue(Integer.toString(100 - i));
            auditFieldList.add(auditField);
        }

        final String xml = JaxbMapper.toXML(new AuditFieldWrapper(auditFieldList));
        LOGGER.debug(xml);

        final AuditFieldWrapper auditFieldWrapper = JaxbMapper.fromXML(xml, AuditFieldWrapper.class);
        for (AuditField auditField : auditFieldWrapper.getAuditFields()) {
            System.err.println(auditField.getBeforeValue());
        }

    }

}
