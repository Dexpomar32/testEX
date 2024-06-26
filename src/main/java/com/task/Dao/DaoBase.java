package com.task.Dao;

import com.task.Dao.Exceptions.DaoException;

import java.util.List;

public interface DaoBase<K, T> {
    List<T> getAll() throws DaoException;
    T getOne(K id) throws DaoException;
    void update(K id, T entity) throws DaoException;
    void delete(K id) throws DaoException;
    void create(T entity) throws DaoException;
}