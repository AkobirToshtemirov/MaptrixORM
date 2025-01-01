package com.maptrix.orm.meta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class FieldModel {
    protected Field field;
    private static final Map<Class<?>, String> typeMap = initTypeMap();


    public FieldModel(Field field) {
        this.field = field;
    }

    public String getName() {
        return field.getName();
    }

    public Field getField() {
        return field;
    }

    public String getColumnType() {
        return typeMap.getOrDefault(field.getType(), "TEXT");
    }

    private static Map<Class<?>, String> initTypeMap() {
        Map<Class<?>, String> map = new HashMap<>();
        map.put(int.class, "INTEGER");
        map.put(Integer.class, "INTEGER");
        map.put(long.class, "BIGINT");
        map.put(Long.class, "BIGINT");
        map.put(double.class, "DOUBLE PRECISION");
        map.put(Double.class, "DOUBLE PRECISION");
        map.put(float.class, "FLOAT");
        map.put(Float.class, "FLOAT");
        map.put(String.class, "VARCHAR(255)");
        map.put(boolean.class, "BOOLEAN");
        map.put(Boolean.class, "BOOLEAN");
        map.put(char.class, "CHAR");
        map.put(Character.class, "CHAR");
        return map;
    }
}
