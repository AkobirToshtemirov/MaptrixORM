package com.maptrix.orm.dao;

import java.sql.Connection;
import java.util.List;

public interface GenericDao<T, ID> {
    T findById(ID id);

    List<T> findAll();

    T create(T entity);

    T update(T entity);

    void delete(T entity);

    void setConnection(Connection connection);
}
