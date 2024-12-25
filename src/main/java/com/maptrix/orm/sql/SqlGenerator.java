package com.maptrix.orm.sql;

import com.maptrix.orm.meta.ColumnField;
import com.maptrix.orm.meta.MetaModel;

import java.util.List;

public class SqlGenerator<T> {

    private final MetaModel<T> metaModel;

    public SqlGenerator(MetaModel<T> metaModel) {
        this.metaModel = metaModel;
    }

    public String generateInsertSql() {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(metaModel.getTableName());
        sb.append(" (");

        boolean isFirst = true;
        for (ColumnField field : metaModel.getColumns()) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append(field.getColumnName());
            isFirst = false;
        }

        sb.append(") VALUES (");
        isFirst = true;
        for (int i = 0; i < metaModel.getColumns().size(); i++) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append("?");
            isFirst = false;
        }
        sb.append(");");

        return sb.toString();
    }

    public String generateInsertSqlWithReturningId() {
        String sqlInsert = generateInsertSql();
        return sqlInsert.substring(0, sqlInsert.length() - 1) + " RETURNING " + metaModel.getPrimaryKey().getName();
    }

    public String generateSelectSql() {
        return "SELECT * FROM " + metaModel.getTableName();
    }

    public String generateSelectSqlWithConditions(String condition) {
        return "SELECT * FROM " + metaModel.getTableName() + " WHERE " + condition;
    }

    public String generateSelectSpecificColumnsSql(List<String> columns) {
        String columnPart = String.join(", ", columns);
        return "SELECT " + columnPart + " FROM " + metaModel.getTableName();
    }

    public String generateJoinedSelectSql(String joinCondition) {
        return "SELECT * FROM " + metaModel.getTableName() + " INNER JOIN otherTable ON " + joinCondition;
    }

    public String generateUpdateSql() {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(metaModel.getTableName());
        sb.append(" SET ");

        boolean isFirst = true;
        for (ColumnField field : metaModel.getColumns()) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append(field.getColumnName()).append(" = ?");
            isFirst = false;
        }

        sb.append(" WHERE ");
        sb.append(metaModel.getPrimaryKey().getName());
        sb.append(" = ?;");

        return sb.toString();
    }

    public String generateDeleteSql() {
        return "DELETE FROM " + metaModel.getTableName() + " WHERE " +
                metaModel.getPrimaryKey().getName() + " = ?";
    }

}
