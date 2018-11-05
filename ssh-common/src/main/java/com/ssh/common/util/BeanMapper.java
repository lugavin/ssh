package com.ssh.common.util;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class BeanMapper {

    private static final Mapper mapper = new DozerBeanMapper();

    public static void map(final Object source, final Object destination) {
        if (source == null) {
            return;
        }
        mapper.map(source, destination);
    }

    public static <T> T map(final Object source, final Class<T> destinationClass) {
        if (source == null) {
            return null;
        }
        return mapper.map(source, destinationClass);
    }

    public static <E, T> List<T> map(final Collection<E> sourceList, final Class<T> destinationClass) {
        if (sourceList == null) {
            return Collections.emptyList();
        }
        List<T> destinationList = new ArrayList<>();
        for (E source : sourceList) {
            destinationList.add(mapper.map(source, destinationClass));
        }
        return destinationList;
    }

}
