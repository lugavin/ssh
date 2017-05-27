package com.ssh.common.core.freemarker;

import com.ssh.common.core.repository.template.WhereDirective;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FreemarkerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerTest.class);

    private Configuration configuration;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass().getClassLoader(), ""));
        configuration.setSharedVariable("where", new WhereDirective());
        Properties props = new Properties();
        props.load(new FileReader(ResourceUtils.getFile("classpath:freemarker.properties")));
        configuration.setSettings(props);
    }

    @Test
    public void test1Macro() throws Exception {
        Map<String, Object> dataModel = new HashMap<>();
        StringWriter out = new StringWriter();
        configuration.getTemplate("macro.ftl").process(dataModel, out);
        LOGGER.info(out.toString());
    }

    @Test
    public void test2Directive() throws Exception {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("id", 101L);
        dataModel.put("name", "Freemarker");
        // String stringTemplate = "SELECT * FROM SYS_USER where 1=1 <#if id??> and id = :id </#if><#if name!!=''> AND name = :name </#if>";
        String stringTemplate = "SELECT * FROM SYS_USER <@where><#if id??>and id = :id </#if> <#if name!!=''> AND name = :name</#if></@where> ORDER BY id DESC";
        StringWriter out = new StringWriter();
        new Template("default", stringTemplate, configuration).process(dataModel, out);
        LOGGER.info(out.toString());
    }

}
