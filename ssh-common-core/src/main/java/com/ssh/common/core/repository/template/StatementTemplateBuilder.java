package com.ssh.common.core.repository.template;

import com.ssh.common.core.xml.MapperBuilder;
import com.ssh.common.dto.DTO;
import com.ssh.common.util.IOUtils;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StatementTemplateBuilder {

    private Map<String, StatementTemplate> templateCache;

    private final MapperBuilder mapperBuilder;

    /**
     * 注意:
     * (1)不需要重复创建 Configuration 实例, 因为它的代价很高, 尤其是会丢失缓存, Configuration 实例是应用级别的单例.
     * 请注意不管一个系统有多少独立的组件来使用 FreeMarker, 它们都会使用自己私有的 Configuration 实例.
     * 当使用多线程应用程序, Configuration 实例中的设置就不能被修改, 它们可以被视作为 "有效的不可改变的" 对象.
     * 为保证实例对其它线程也可用, 可以通过final或volatile字段来声明实例, 或者通过线程安全的IoC容器, 但不能作为普通字段.
     * (2)在多线程运行环境中, Configuration 实例, Template 实例和数据模型应该是永远不能改变(只读)的对象.
     * 也就是说, 创建和初始化它们(如使用 set... 方法)之后, 就不能再修改它们了(比如不能再次调用 set... 方法), 这就允许我们在多线程环境中避免代价很大的同步锁问题.
     * 要小心 Template 实例, 当使用了 Configuration.getTemplate 方法获得 Template 一个实例时, 也许得到的是从模板缓存中缓存的实例,
     * 这些实例都已经被其他线程使用了, 所以不要调用它们的 set... 方法(当然调用 process 方法还是不错的).
     */
    private final Configuration configuration;

    public StatementTemplateBuilder(MapperBuilder mapperBuilder, Configuration configuration) {
        this.mapperBuilder = mapperBuilder;
        this.configuration = configuration;
    }

    /**
     * 根据StatementId获取StatementTemplate对象
     */
    public StatementTemplate getStatementTemplate(String statement) {
        return templateCache.get(statement);
    }

    public Set<String> getStatementKeys() {
        return Collections.unmodifiableSet(templateCache.keySet());
    }

    public Map<String, StatementTemplate> getTemplateCache() {
        return Collections.unmodifiableMap(templateCache);
    }

    /**
     * FreeMarker模板处理(动态HQL/SQL查询): 移除查询模板中的FreeMarker标记
     *
     * @param statementTemplate 自定义的模板包装类
     * @param dto               数据模型(通常是Map或JavaBean)
     * @return 经过FreeMarker模板引擎处理后的HQL/SQL字符串
     */
    public String processTemplate(StatementTemplate statementTemplate, DTO dto) {
        StringWriter out = null;
        try {
            out = new StringWriter();
            statementTemplate.getTemplate().process(dto, out);
            return out.toString();
        } catch (TemplateException | IOException e) {
            throw new RuntimeException("An error occurred while processing the Freemarker template.", e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 当Spring设置完该Bean所有属性后调用该方法
     * 实现InitializingBean接口与通过Spring配置文件指定init-method来进行初始化的区别:
     * (1)实现InitializingBean接口是直接调用afterPropertiesSet()方法, 而指定init-method方式则是通过反射来执行.
     * (2)直接调用afterPropertiesSet()方法比通过反射调用init-method所指定的方法效率相对要高些, 但init-method方式消除了对Spring的依赖.
     */
    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        if (mapperBuilder == null) {
            throw new IllegalArgumentException("Property 'mapperBuilder' is required");
        }
        if (configuration == null) {
            // configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            // configuration.setSharedVariable("where", new WhereDirective());
            throw new IllegalArgumentException("Property 'configuration' is required");
        }
        templateCache = new HashMap<>();
        mapperBuilder.init();
        Map<String, String> hqlMap = mapperBuilder.getHqlMap();
        Map<String, String> sqlMap = mapperBuilder.getSqlMap();
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        for (Map.Entry<String, String> entry : hqlMap.entrySet()) {
            String queryKey = entry.getKey();
            String queryTemplate = entry.getValue();
            templateLoader.putTemplate(queryKey, queryTemplate);
            Template template = new Template(queryKey, queryTemplate, configuration);
            templateCache.put(queryKey, new StatementTemplate(StatementTemplate.Type.HQL, template));
        }
        for (Map.Entry<String, String> entry : sqlMap.entrySet()) {
            String queryKey = entry.getKey();
            String queryTemplate = entry.getValue();
            templateLoader.putTemplate(queryKey, queryTemplate);
            Template template = new Template(queryKey, queryTemplate, configuration);
            templateCache.put(queryKey, new StatementTemplate(StatementTemplate.Type.SQL, template));
        }
        configuration.setTemplateLoader(templateLoader);
    }

}
