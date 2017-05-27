package com.ssh.common.core;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PatternTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatternTest.class);

    private static final String COUNT_REPLACEMENT_TEMPLATE = "SELECT COUNT(*) %s";
    // private static final Pattern ORDER_BY_PATTERN = Pattern.compile(".*ORDER\\s+BY\\s+.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern ORDER_BY_PATTERN = Pattern.compile("\\s+ORDER\\s+BY\\s+.*", Pattern.CASE_INSENSITIVE);

    private static final String queryString = "SELECT * FROM SYS_USER\n WHERE id = :id\n AND code = :code\n ORDER BY id DESC";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test1RemoveOrderBy() throws Exception {
        LOGGER.debug(removeOrderBy(queryString));
    }

    @Test
    public void test2RemoveSelect() throws Exception {
        LOGGER.debug(removeSelect(queryString));
    }

    @Test
    public void test3CreateCount() throws Exception {
        LOGGER.debug(String.format(COUNT_REPLACEMENT_TEMPLATE, removeSelect(removeOrderBy(queryString))));
    }

    private String removeSelect(String queryString) {
        return queryString.substring(queryString.toUpperCase(Locale.ENGLISH).indexOf("FROM"));
    }

    private String removeOrderBy(String queryString) {
        Matcher matcher = ORDER_BY_PATTERN.matcher(queryString);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
