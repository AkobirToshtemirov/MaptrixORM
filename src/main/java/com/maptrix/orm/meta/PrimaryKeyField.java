package com.maptrix.orm.meta;

import com.maptrix.orm.annotations.Id;

import java.lang.reflect.Field;

public class PrimaryKeyField extends FieldModel {

    public PrimaryKeyField(Field field) {
        super(field);
    }

    public String getColumnName() {
        Id id = field.getAnnotation(Id.class);
        return (id != null && !id.name().isEmpty()) ? id.name() : getName();
    }
}
