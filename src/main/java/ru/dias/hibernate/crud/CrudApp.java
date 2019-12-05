package ru.dias.hibernate.crud;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.dias.hibernate.validation.PrepareDataApp;

public class CrudApp {
    public static void main(String[] args) throws Exception {
        PrepareDataApp.forcePrepareData();

        Long maxId = null;

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        Session session = null;
        try {
            // запись
            session = factory.getCurrentSession();
            session.beginTransaction();
            SimpleItem newSimpleItem = new SimpleItem("Dragon statue", 100000);
            session.save(newSimpleItem);
            session.getTransaction().commit();

            // чтение по id = 1
            session = factory.getCurrentSession();
            session.beginTransaction();
            SimpleItem simpleItemFromDb = session.get(SimpleItem.class, 1L);
            session.getTransaction().commit();

            // изменение
            session = factory.getCurrentSession();
            session.beginTransaction();
            maxId = session.createQuery("SELECT MAX(s.id) FROM SimpleItem s", Long.class).getSingleResult();
            SimpleItem simpleItemForUpdate = session.createQuery("SELECT s FROM SimpleItem s WHERE s.id = :id", SimpleItem.class)
                    .setParameter("id", maxId)
                    .getSingleResult();
            simpleItemForUpdate.setPrice(simpleItemForUpdate.getPrice() + 100);
            session.getTransaction().commit();

            // удаление
            session = factory.getCurrentSession();
            session.beginTransaction();
            maxId = session.createQuery("SELECT MAX(s.id) FROM SimpleItem s", Long.class).getSingleResult();
            session.delete(session.get(SimpleItem.class, maxId));
            session.getTransaction().commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            factory.close();
            if (session != null) {
                session.close();
            }
        }
    }
}
