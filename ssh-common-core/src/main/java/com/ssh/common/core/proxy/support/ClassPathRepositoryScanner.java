package com.ssh.common.core.proxy.support;

import com.ssh.common.core.proxy.RepositoryFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * @author Gavin
 * @see org.mybatis.spring.mapper.ClassPathMapperScanner
 */
public class ClassPathRepositoryScanner extends ClassPathBeanDefinitionScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathRepositoryScanner.class);

    private RepositoryFactoryBean repositoryFactoryBean = new RepositoryFactoryBean();

    private Class<?> markerInterface;

    private Class<? extends Annotation> annotationClass;

    public ClassPathRepositoryScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void setMarkerInterface(Class<?> markerInterface) {
        this.markerInterface = markerInterface;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public void setRepositoryFactoryBean(RepositoryFactoryBean repositoryFactoryBean) {
        this.repositoryFactoryBean = repositoryFactoryBean;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            LOGGER.warn("Skipping RepositoryFactoryBean with name '{}' and '{}' repositoryInterface. Bean already defined with the same name!", beanName, beanDefinition.getBeanClassName());
            return false;
        }
    }

    /**
     * Calls the parent search that will search and register all the candidates.
     * Then the registered objects are post processed to set them as
     * MapperFactoryBeans
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            LOGGER.warn("No JPA repository was found in '{}' package. Please check your configuration.", Arrays.toString(basePackages));
        } else {
            processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            LOGGER.debug("Creating RepositoryFactoryBean with name '{}' and '{}' repositoryInterface", holder.getBeanName(), definition.getBeanClassName());
            // the repository interface is the original class of the bean
            // but the actual class of the bean is RepositoryFactoryBean
            definition.getPropertyValues().add("repositoryInterface", definition.getBeanClassName());
            definition.setBeanClass(repositoryFactoryBean.getClass());
            LOGGER.debug("Enabling autowire by type for RepositoryFactoryBean with name '{}'.", holder.getBeanName());
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
    }

    public void registerFilters() {
        boolean acceptAllInterfaces = true;

        // if specified, use the given annotation and / or marker interface
        if (annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(annotationClass));
            acceptAllInterfaces = false;
        }

        // override AssignableTypeFilter to ignore matches on the actual marker interface
        if (markerInterface != null) {
            addIncludeFilter(new AssignableTypeFilter(markerInterface) {
                @Override
                protected boolean matchClassName(String className) {
                    return false;
                }
            });
            acceptAllInterfaces = false;
        }

        if (acceptAllInterfaces) {
            // default include filter that accepts all classes
            addIncludeFilter(new TypeFilter() {
                @Override
                public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                    return true;
                }
            });
        }

        // exclude package-info.java
        addExcludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                String className = metadataReader.getClassMetadata().getClassName();
                return className.endsWith("package-info");
            }
        });
    }

}
