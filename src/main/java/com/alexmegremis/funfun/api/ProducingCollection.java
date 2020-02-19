package com.alexmegremis.funfun.api;

import org.springframework.util.CollectionUtils;

import java.util.*;

public interface ProducingCollection {

    static <T> Set<T> setOf(T... elements) {
        return new HashSet<T>(CollectionUtils.arrayToList(elements));
    }

    static <T> List<T> listOf(T... elements) {
        return CollectionUtils.arrayToList(elements);
    }
}

