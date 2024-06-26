package com.task.Dao.DaoImpl;

import com.task.Dao.DaoModels.DaoProduct;
import com.task.Model.Product;
import com.task.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class ProductImpl implements DaoProduct {
    private final static String HQL_SELECT_ALL_FROM_PRODUCT = "FROM Product";
    private final SessionFactory sessionFactory;

    public ProductImpl() {
        this.sessionFactory = HibernateUtil.getInstance().getSessionFactory();
    }

    @Override
    public List<Product> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Product> query = session.createQuery(HQL_SELECT_ALL_FROM_PRODUCT, Product.class);
            return query.list();
        }
    }

    @Override
    public Product getOne(String id) {
        try (Session session = sessionFactory.openSession()) {
            return session.bySimpleNaturalId(Product.class)
                    .load(id);
        }
    }

    @Override
    public void update(String id, Product entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Product product = session.bySimpleNaturalId(Product.class)
                    .load(id);

            try {
                product.setName(entity.getName());
                product.setPrice(entity.getPrice());
                product.setCategory(entity.getCategory());
                session.merge(product);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void delete(String id) {
        try (Session session = sessionFactory.openSession()) {
            Product product = session.bySimpleNaturalId(Product.class)
                    .load(id);
            session.beginTransaction();
            session.evict(product);
            session.remove(product);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Product entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }
}
