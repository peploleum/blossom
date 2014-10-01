package blossom.persistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Since we use Tomcat as a container we need to handle ourselves persistence
 * management with an application-wide manager
 *
 * @author peploleum
 *
 */
public class EntityManagerFactorySingleton {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BlossomLocal");
    static EntityManagerFactorySingleton INSTANCE;

    EntityManagerFactorySingleton() {
    }

    public static EntityManagerFactorySingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EntityManagerFactorySingleton();
        }
        return INSTANCE;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return this.entityManagerFactory;
    }
}