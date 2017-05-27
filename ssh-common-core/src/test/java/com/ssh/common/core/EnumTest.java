package com.ssh.common.core;

import com.ssh.common.core.enums.Action;
import org.apache.commons.lang3.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EnumTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnumTest.class);

    @Test
    public void testEnum() throws Exception {
        Action action = Action.valueOf(StringUtils.upperCase("insert"));
        LOGGER.info("Action => {}", action);
    }

}
