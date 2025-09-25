package org.uthmaniv.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.uthmaniv.QStudent;
import org.uthmaniv.exception.StudentNotFoundException;
import org.uthmaniv.models.Student;

public class StudentRepository {
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public StudentRepository(EntityManager entityManager, JPAQueryFactory queryFactory) {
        this.entityManager = entityManager;
        this.queryFactory = queryFactory;
    }

    public void save(Student s) {
        entityManager.getTransaction().begin();
        entityManager.persist(s);
        entityManager.getTransaction().commit();
    }

    public Student findById(String id) {
        return entityManager.find(Student.class, id);
    }

    public Student findByName(String firstName, String lastName) throws StudentNotFoundException {
        QStudent student = QStudent.student;

        Student result = queryFactory
                .selectFrom(student)
                .where(student.firstName.eq(firstName)
                        .and(student.lastName.eq(lastName)))
                .fetchOne();

        if (result == null) {
            throw new StudentNotFoundException(
                    "Student not found with name: " + firstName + " " + lastName
            );
        }

        return result;
    }


}

