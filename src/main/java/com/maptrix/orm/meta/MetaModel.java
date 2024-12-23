package com.maptrix.orm.meta;

import com.maptrix.orm.annotations.*;
import com.maptrix.orm.meta.enums.RelationshipType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MetaModel<T> {
    private final Class<T> clazz;
    private final List<ColumnField> columns;
    private final List<RelationshipField> relationships;
    private PrimaryKeyField primaryKey;
    private final List<TransientField> transients;

    public MetaModel(Class<T> clazz) {
        this.clazz = clazz;
        this.columns = new ArrayList<>();
        this.relationships = new ArrayList<>();
        this.transients = new ArrayList<>();
        processFields();
    }

    public static <T> MetaModel of(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException("Class " + clazz.getSimpleName() + " must be annotated with @Entity");
        }
        return new MetaModel(clazz);
    }

    private void processFields() {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                columns.add(new ColumnField(field));
            } else if (field.isAnnotationPresent(Id.class)) {
                primaryKey = new PrimaryKeyField(field);
            } else if (field.isAnnotationPresent(Transient.class)) {
                transients.add(new TransientField(field));
            } else if (field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToOne.class) ||
                    field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(ManyToMany.class)) {
                relationships.add(new RelationshipField(field, determineRelationshipType(field)));
            }
        }

        if (primaryKey == null) {
            throw new IllegalStateException("No primary key found for " + clazz.getName());
        }
    }

    private RelationshipType determineRelationshipType(Field field) {
        if (field.isAnnotationPresent(OneToMany.class)) return RelationshipType.ONE_TO_MANY;
        if (field.isAnnotationPresent(ManyToOne.class)) return RelationshipType.MANY_TO_ONE;
        if (field.isAnnotationPresent(OneToOne.class)) return RelationshipType.ONE_TO_ONE;
        if (field.isAnnotationPresent(ManyToMany.class)) return RelationshipType.MANY_TO_MANY;
        throw new IllegalStateException("Unknown relationship type");
    }

    public String getTableName() {
        if (clazz.isAnnotationPresent(Table.class)) {
            return clazz.getAnnotation(Table.class).name();
        }

        return clazz.getSimpleName().toUpperCase();
    }

    public List<ColumnField> getColumns() {
        return columns;
    }

    public List<RelationshipField> getRelationships() {
        return relationships;
    }

    public PrimaryKeyField getPrimaryKey() {
        return primaryKey;
    }

    public List<TransientField> getTransients() {
        return transients;
    }

}
