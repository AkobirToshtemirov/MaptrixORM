package com.maptrix.orm.util;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReflectionUtil {
    public static void setEntityField(Object entity, String fieldName, Object fieldValue) throws ReflectiveOperationException {
        Field field = entity.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(entity, fieldValue);
    }

    public static Object getEntityField(Object entity, String fieldName) throws ReflectiveOperationException {
        Field field = entity.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(entity);
    }

    public static void bindParameters(PreparedStatement stmt, Object entity, Field[] fields) throws ReflectiveOperationException, SQLException {
        int parameterIndex = 1;
        for (Field field : fields) {
            field.setAccessible(true);
            stmt.setObject(parameterIndex++, field.get(entity));
        }
    }
}
