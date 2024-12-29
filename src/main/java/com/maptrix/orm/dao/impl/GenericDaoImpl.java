package com.maptrix.orm.dao.impl;

import com.maptrix.orm.dao.GenericDao;
import com.maptrix.orm.exceptions.DataAccessException;
import com.maptrix.orm.meta.MetaModel;
import com.maptrix.orm.sql.SqlGenerator;
import com.maptrix.orm.util.DataSourceUtil;
import com.maptrix.orm.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenericDaoImpl<T, ID> implements GenericDao<T, ID> {
    private Connection connection;
    private final MetaModel<T> metaModel;
    private final SqlGenerator<T> sqlGenerator;

    public GenericDaoImpl(Class<T> clazz) {
        this.metaModel = MetaModel.of(clazz);
        this.sqlGenerator = new SqlGenerator<>(metaModel);
    }

    @Override
    public T findById(ID id) {
        String sql = sqlGenerator.generateSelectSql() + " WHERE " + metaModel.getPrimaryKey().getName() + " = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            return mapResultSetToEntity(rs);
        } catch (SQLException | ReflectiveOperationException e) {
            throw new DataAccessException("Failed to find entity by ID", e);
        }
    }

    @Override
    public List<T> findAll() {
        String sql = sqlGenerator.generateSelectSql();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
            return results;
        } catch (SQLException | ReflectiveOperationException e) {
            throw new DataAccessException("Failed to find all entities", e);
        }
    }

    @Override
    public T create(T entity) {
        String sql = sqlGenerator.generateInsertSql();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setStatementParameters(stmt, entity);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                setEntityId(entity, rs.getObject(1));
            }
            return entity;
        } catch (SQLException | ReflectiveOperationException e) {
            throw new DataAccessException("Failed to create entity", e);
        }
    }

    @Override
    public T update(T entity) {
        String sql = sqlGenerator.generateUpdateSql();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setStatementParameters(stmt, entity);
            stmt.executeUpdate();
            return entity;
        } catch (SQLException | ReflectiveOperationException e) {
            throw new DataAccessException("Failed to update entity", e);
        }
    }

    @Override
    public void delete(T entity) {
        String sql = sqlGenerator.generateDeleteSql();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, getEntityId(entity));
            stmt.executeUpdate();
        } catch (SQLException | ReflectiveOperationException e) {
            throw new DataAccessException("Failed to delete entity", e);
        }
    }

    private T mapResultSetToEntity(ResultSet rs) throws ReflectiveOperationException, SQLException {
        T entity = metaModel.createInstance();
        for (Field field : metaModel.getFields()) {
            Object value = rs.getObject(field.getName());
            ReflectionUtil.setEntityField(entity, field.getName(), value);
        }
        return entity;
    }

    private void setEntityId(T entity, Object id) throws ReflectiveOperationException {
        ReflectionUtil.setEntityField(entity, metaModel.getPrimaryKey().getField().getName(), id);
    }

    private Object getEntityId(T entity) throws ReflectiveOperationException {
        return ReflectionUtil.getEntityField(entity, metaModel.getPrimaryKey().getField().getName());
    }

    private void setStatementParameters(PreparedStatement stmt, T entity) throws ReflectiveOperationException, SQLException {
        Field[] fields = metaModel.getFields().toArray(new Field[0]);
        ReflectionUtil.bindParameters(stmt, entity, fields);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private Connection getConnection() throws SQLException {
        if (this.connection != null) {
            if (this.connection.isClosed()) {
                throw new SQLException("Provided connection is closed.");
            }
            return this.connection;
        }
        throw new IllegalStateException("Connection is not set. This operation requires a managed connection from EntityManager.");
    }
}
