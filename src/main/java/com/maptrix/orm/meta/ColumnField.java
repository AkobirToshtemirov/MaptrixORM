package com.maptrix.orm.meta;

import com.maptrix.orm.annotations.Column;

import java.lang.reflect.Field;

public class ColumnField extends FieldModel {

    public ColumnField(Field field) {
        super(field);
    }

    public String getColumnName() {
        Column column = field.getAnnotation(Column.class);
        return column != null ? column.name() : getName();
    }

}
