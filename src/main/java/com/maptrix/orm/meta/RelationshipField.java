package com.maptrix.orm.meta;

import com.maptrix.orm.annotations.JoinColumn;
import com.maptrix.orm.annotations.ManyToMany;
import com.maptrix.orm.annotations.OneToMany;
import com.maptrix.orm.annotations.OneToOne;
import com.maptrix.orm.meta.enums.RelationshipType;

import java.lang.reflect.Field;

public class RelationshipField extends FieldModel {
    private final RelationshipType type;
    private final String mappedBy;

    public RelationshipField(Field field, RelationshipType type) {
        super(field);
        this.type = type;
        this.mappedBy = extractMappedBy();
    }

    private String extractMappedBy() {
        return switch (type) {
            case ONE_TO_ONE -> field.getAnnotation(OneToOne.class).mappedBy();
            case ONE_TO_MANY -> field.getAnnotation(OneToMany.class).mappedBy();
            case MANY_TO_ONE -> extractJoinColumn();
            case MANY_TO_MANY -> field.getAnnotation(ManyToMany.class).mappedBy();
        };
    }

    private String extractJoinColumn() {
        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
        return joinColumn != null ? joinColumn.name() : "id";
    }

    public RelationshipType getType() {
        return type;
    }

    public String getMappedBy() {
        return mappedBy;
    }

}
