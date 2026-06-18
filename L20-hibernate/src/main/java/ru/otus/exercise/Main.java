package ru.otus.exercise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.HibernateUtils;
import ru.otus.exercise.model.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try (var sessionFactory =
                HibernateUtils.buildSessionFactory(OtusStudent.class, Avatar.class, EMail.class, Course.class)) {
/*
            var em = sessionFactory.createEntityManager();
            var tx = em.getTransaction();
            tx.begin();
            System.out.println("\n\n---------------");

            var avatar = new Avatar(0, "xxxx", null);
            var student = new OtusStudent(0, "test", avatar, new ArrayList<>(), new ArrayList<>());
            avatar.setStudent(student);
            var email = new EMail(0, "test@test.com", student);
            student.getEmails().add(email);

            em.persist(student);
            //em.persist(email);
            em.flush();
            em.clear();

            System.out.println("\n\n========");
            var found = em.find(OtusStudent.class, 1);

            System.out.println("\n\n==toStr");
            System.out.println(found.getEmails().getFirst().getStudent());

            tx.commit();


*/
            logger.info("Statistics:{}", sessionFactory.getStatistics());
        }
    }
}
