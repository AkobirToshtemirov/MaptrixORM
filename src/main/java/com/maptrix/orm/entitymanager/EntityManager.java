package com.maptrix.orm.entitymanager;

public interface EntityManager<T> {
    T find(Class<T> entityClass, Object primaryKey);

    void persist(T entity);

    T merge(T entity);

    void remove(T entity);

    void beginTransaction();

    void commit();

    void rollback();

}
