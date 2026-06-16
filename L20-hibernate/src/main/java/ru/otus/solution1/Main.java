package ru.otus.solution1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.HibernateUtils;
import ru.otus.solution1.model.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try (var sessionFactory =
                HibernateUtils.buildSessionFactory(OtusStudent.class, Avatar.class, EMail.class, Course.class)) {

            var em = sessionFactory.createEntityManager();
            var tx = em.getTransaction();
            tx.begin();
            em.persist(new OtusStudent(0, "test"));
            em.flush();
            var found = em.find(OtusStudent.class, 0);
            System.out.println(found.toString());
            tx.commit();

            logger.info("Statistics:{}", sessionFactory.getStatistics());
        }
    }
}
