package com.maptrix.orm.core;

import com.maptrix.orm.annotations.Column;
import com.maptrix.orm.annotations.Table;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MetadataHandler {
    public static Map<String, String> getTableMapping(Class<?> clazz) {
        Map<String, String> mapping = new HashMap<>();

        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    mapping.put(field.getName(), column.name());
                }
            }
        }

        return mapping;
    }
}
