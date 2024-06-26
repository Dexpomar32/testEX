package com.task.Utils;

import com.task.Config.HibernateConfig;
import lombok.Getter;
import org.hibernate.SessionFactory;

public class HibernateUtil {
    @Getter
    private static final HibernateUtil instance = new HibernateUtil();
    @Getter
    private final SessionFactory sessionFactory;

    private HibernateUtil() {
        sessionFactory = new HibernateConfig().buildSessionFactory();
    }
}