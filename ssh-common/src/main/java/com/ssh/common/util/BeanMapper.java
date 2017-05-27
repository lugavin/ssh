package com.ssh.common.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class BeanMapper {

    public static void map(final Object source, final Object destination) {
        if (source == null) {
            return;
        }
        BeanUtils.copyProperties(source, destination);
    }

    public static <T> T map(final Object source, final Class<T> destinationClass) {
        if (source == null) {
            return null;
        }
        T destination = BeanUtils.instantiate(destinationClass);
        BeanUtils.copyProperties(source, destination);
        return destination;
    }

    public static <E, T> List<T> map(final Collection<E> sourceList, final Class<T> destinationClass) {
        if (sourceList == null) {
            return Collections.emptyList();
        }
        List<T> destinationList = new ArrayList<>();
        for (E source : sourceList) {
            T destination = BeanUtils.instantiate(destinationClass);
            BeanUtils.copyProperties(source, destination);
            destinationList.add(destination);
        }
        return destinationList;
    }

}
