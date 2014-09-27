package blossom.test;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import blossom.persistence.entity.CharacterEntity;

public class PersistenceTest {
    private static final Logger LOGGER = Logger.getLogger(PersistenceTest.class.getName());

    public static void main(final String[] args) {
        // EntityManagerFactory createEntityManager
        final EntityManagerFactory createEntityManagerFactory = Persistence.createEntityManagerFactory("BlossomLocal");
        final EntityManager entityManager = createEntityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            try {

                final CharacterEntity character = new CharacterEntity();
                character.setId(UUID.randomUUID().toString());
                character.setName("Farnsworth");
                character.setCatchphrase("Ooooh my yes!");
                character.setSize(70);
                entityManager.persist(character);
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        } finally {
            entityManager.close();
        }
    }
}
