package com.maptrix.orm.meta;

import java.lang.reflect.Field;

public abstract class FieldModel {
    protected Field field;

    public FieldModel(Field field) {
        this.field = field;
    }

    public String getName() {
        return field.getName();
    }

    public Field getField() {
        return field;
    }
}
