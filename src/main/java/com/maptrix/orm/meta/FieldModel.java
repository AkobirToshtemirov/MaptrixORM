package com.maptrix.orm.meta;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class FieldModel {
    protected Field field;
    private static Map<Class<?>, String> typeMap = Map.of(
            int.class, "INTEGER",
            long.class, "BIGINT",
            double.class, "DOUBLE PRECISION",
            float.class, "FLOAT",
            String.class, "VARCHAR(255)",
            boolean.class, "BOOLEAN"
    );


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
}
