package database;

import domain.Person;
import java.util.Properties;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

class HibernateUtil {

  private HibernateUtil(){

  }

  private static SessionFactory sessionFactory;

  private static SessionFactory getSessionFactory() {
    if (sessionFactory != null) {
      return sessionFactory;
    }

    Properties properties = new Properties();
    properties.put(Environment.DRIVER, "org.postgresql.Driver");
    properties.put(Environment.URL,
        "jdbc:postgresql://" + System.getenv("DB_HOST") + ":" + System.getenv("DB_PORT") + "/" + System
            .getenv("DB_NAME"));
    properties.put(Environment.USER, System.getenv("DB_USER"));
    properties.put(Environment.PASS, System.getenv("DB_PASSWORD"));
    properties.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL9Dialect");
    properties.put(Environment.SHOW_SQL, "true");
    properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
    properties.put(Environment.HBM2DDL_AUTO, "update");

    Configuration configuration = new Configuration();
    configuration.setProperties(properties);
    configuration.addAnnotatedClass(Person.class);
    StandardServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
    sessionFactory = configuration.buildSessionFactory(registry);
    return sessionFactory;
  }

  static Session openSession() {
    return getSessionFactory().openSession();
  }
}
