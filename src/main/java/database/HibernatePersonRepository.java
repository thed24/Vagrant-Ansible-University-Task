package database;

import domain.Person;
import domain.Repository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernatePersonRepository implements Repository {

  public HibernatePersonRepository() {
    if (getAll().isEmpty()) {
      Person owner = new Person(1, "Dominic", true);
      add(owner);
    }
  }

  @Override
  public Optional<Person> getByID(Integer id){
    try (Session session = HibernateUtil.openSession()) {
      return Optional.ofNullable(session.get(Person.class, id));
    }
  }

  @Override
  public Set<Person> getAll() {
    try (Session session = HibernateUtil.openSession()) {
      String query = "from Person";
      return new HashSet<>(session.createQuery(query, Person.class).list());
    }
  }

  @Override
  public void delete(Person person) {
    try (Session session = HibernateUtil.openSession()) {
      Transaction transaction = session.beginTransaction();
      session.delete(person);
      transaction.commit();
    }
  }

  @Override
  public void update(Person person) {
    try (Session session = HibernateUtil.openSession()) {
      Transaction transaction = session.beginTransaction();
      session.update(person);
      transaction.commit();
    }
  }

  @Override
  public void add(Person person) {
    try (Session session = HibernateUtil.openSession()) {
      Transaction transaction = session.beginTransaction();
      session.save(person);
      transaction.commit();
    }
  }
}