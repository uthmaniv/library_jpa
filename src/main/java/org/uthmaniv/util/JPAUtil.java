package org.uthmaniv;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JPAUtil {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("libraryPU");

    private JPAUtil() {}

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void shutdown() {
        emf.close();
    }
}
