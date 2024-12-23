package com.maptrix.orm.meta;

import com.maptrix.orm.annotations.Column;

import java.lang.reflect.Field;

public class ColumnField extends FieldModel{

    public ColumnField(Field field) {
        super(field);
    }

    public String getColumnName() {
        return field.getAnnotation(Column.class).name();
    }

}
