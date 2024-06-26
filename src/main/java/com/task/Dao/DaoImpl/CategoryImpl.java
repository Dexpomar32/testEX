package com.task.Dao.DaoImpl;

import com.task.Dao.DaoModels.DaoCategory;
import com.task.Model.Category;
import com.task.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class CategoryImpl implements DaoCategory {
    private final static String HQL_SELECT_ALL_FROM_CATEGORY = "FROM Category";
    private final SessionFactory sessionFactory;

    public CategoryImpl() {
        this.sessionFactory = HibernateUtil.getInstance().getSessionFactory();
    }

    @Override
    public List<Category> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Category> query = session.createQuery(HQL_SELECT_ALL_FROM_CATEGORY, Category.class);
            return query.list();
        }
    }

    @Override
    public Category getOne(String id) {
        try (Session session = sessionFactory.openSession()) {
            return session.bySimpleNaturalId(Category.class)
                    .load(id);
        }
    }

    @Override
    public void update(String id, Category entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Category category = session.bySimpleNaturalId(Category.class)
                    .load(id);

            try {
                category.setName(entity.getName());
                session.merge(category);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void delete(String id) {
        try (Session session = sessionFactory.openSession()) {
            Category category = session.bySimpleNaturalId(Category.class)
                    .load(id);
            session.beginTransaction();
            session.evict(category);
            session.remove(category);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Category entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }
}
