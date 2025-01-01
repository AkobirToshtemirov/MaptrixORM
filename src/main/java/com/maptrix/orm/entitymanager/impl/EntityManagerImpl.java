package com.maptrix.orm.entitymanager.impl;

import com.maptrix.orm.dao.GenericDao;
import com.maptrix.orm.entitymanager.EntityManager;
import com.maptrix.orm.exceptions.ConnectionException;
import com.maptrix.orm.exceptions.TransactionException;
import com.maptrix.orm.util.DataSourceUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class EntityManagerImpl<T> implements EntityManager<T> {

    private static final Logger LOGGER = Logger.getLogger(EntityManagerImpl.class.getName());
    private final GenericDao<T, Object> dao;
    private Connection connection;

    public EntityManagerImpl(GenericDao<T, Object> dao) {
        this.dao = dao;
        ensureConnection();
    }

    private void ensureConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DataSourceUtil.getConnection();
            }
            dao.setConnection(connection);
        } catch (SQLException e) {
            LOGGER.severe("Cannot establish a connection: " + e.getMessage());
            throw new ConnectionException("Unable to establish a connection", e);
        }
    }

    @Override
    public T find(Class<T> entityClass, Object primaryKey) {
        ensureConnection();
        return dao.findById(primaryKey);
    }

    @Override
    public void persist(T entity) {
        ensureConnection();
        dao.create(entity);
    }

    @Override
    public T merge(T entity) {
        ensureConnection();
        return dao.update(entity);
    }

    @Override
    public void remove(T entity) {
        ensureConnection();
        dao.delete(entity);
    }

    @Override
    public void beginTransaction() {
        ensureConnection();
        try {
            if (!connection.isValid(5)) {
                throw new SQLException("Connection is not valid.");
            }
            connection.setAutoCommit(false);
            LOGGER.info("Transaction started.");
        } catch (SQLException e) {
            throw new TransactionException("Could not start transaction", e);
        }
    }

    @Override
    public void commit() {
        try {
            connection.commit();
            LOGGER.info("Transaction committed.");
        } catch (SQLException e) {
            throw new TransactionException("Could not commit transaction", e);
        } finally {
            cleanup();
        }
    }

    @Override
    public void rollback() {
        try {
            connection.rollback();
            LOGGER.info("Transaction rolled back.");
        } catch (SQLException e) {
            throw new TransactionException("Could not rollback transaction", e);
        } finally {
            cleanup();
        }
    }

    private void cleanup() {
        try {
            if (connection != null) {
                connection.close();
                LOGGER.info("Connection closed.");
                connection = null;
            }
            dao.setConnection(null);
        } catch (SQLException e) {
            LOGGER.severe("Failed to close connection");
            throw new TransactionException("Failed to close connection", e);
        }
    }
}
