package test;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import server.login.domain.User;

public class Test {

    public static void main(String[] args) {

        try {
            SessionFactory sf =
                new Configuration().configure().buildSessionFactory();
            Session session = sf.openSession();
            Transaction tx = session.beginTransaction();

            for (int i = 0; i < 200; i++) {
                User customer = new User();
                customer.setName("customer" + i);
                customer.setPassword("customer");
                session.save(customer);
            }

            tx.commit();
            session.close();

        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
