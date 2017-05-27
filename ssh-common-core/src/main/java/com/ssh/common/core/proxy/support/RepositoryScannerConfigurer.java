package com.ssh.common.core.proxy.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;

import static org.springframework.util.Assert.notNull;

/**
 * A component provider that scans the classpath from a base package. It then
 * applies exclude and include filters to the resulting classes to find candidates.
 *
 * @author Gavin
 * @see org.mybatis.spring.mapper.MapperScannerConfigurer
 */
public class RepositoryScannerConfigurer implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryScannerConfigurer.class);

    private String basePackage;

    private Class<?> markerInterface;

    private Class<? extends Annotation> annotationClass;

    private ApplicationContext applicationContext;

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setMarkerInterface(Class<?> markerInterface) {
        this.markerInterface = markerInterface;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        ClassPathRepositoryScanner scanner = new ClassPathRepositoryScanner(registry);
        scanner.setMarkerInterface(markerInterface);
        scanner.setAnnotationClass(annotationClass);
        scanner.setResourceLoader(applicationContext);
        scanner.registerFilters();
        scanner.scan(StringUtils.tokenizeToStringArray(basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(basePackage, "Property 'basePackage' is required");
    }

}
