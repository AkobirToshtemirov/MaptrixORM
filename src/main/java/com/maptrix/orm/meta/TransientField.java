package com.maptrix.orm.meta;

import java.lang.reflect.Field;

public class TransientField extends FieldModel{

    public TransientField(Field field) {
        super(field);
    }
}
