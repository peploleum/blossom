package blossom.persistence;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.Test;

import blossom.persistence.entity.AbstractBlossomEntity;
import blossom.persistence.entity.CharacterEntity;
import blossom.persistence.entity.Symbol;

public class PersistenceFullTest {

    @Test
    public void purgeTestDatabase() {
        final EntityManagerFactory createEntityManagerFactory = Persistence.createEntityManagerFactory("BlossomTestLocal");
        final EntityManager entityManager = createEntityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            final Query nameQuery = entityManager.createNamedQuery("CharacterEntity.findAll");
            @SuppressWarnings("unchecked")
            final List<CharacterEntity> resultList = nameQuery.getResultList();
            for (final CharacterEntity characterEntity : resultList) {
                entityManager.remove(characterEntity);
            }
            final Query symbolQuery = entityManager.createNamedQuery("Symbol.findAll");
            @SuppressWarnings("unchecked")
            final List<Symbol> symbols = symbolQuery.getResultList();
            for (final Symbol symbol : symbols) {
                entityManager.remove(symbol);
            }

            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    @Test
    public void shouldLinkEntities() throws IOException {
        final EntityManagerFactory createEntityManagerFactory = Persistence.createEntityManagerFactory("BlossomTestLocal");
        final EntityManager entityManager = createEntityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            final CharacterEntity character = new CharacterEntity();
            final String characterId = UUID.randomUUID().toString();
            character.setId(characterId);
            character.setName("farnsworth");
            character.setCatchphrase("Ooooh my yes!");
            final InputStream symbolStream = PersistenceFullTest.class.getResourceAsStream("/farnsworth.png");
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final byte[] buffer = new byte[1024];
            int nRead;
            while ((nRead = symbolStream.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, nRead);
            }

            final Symbol symbol = new Symbol();
            symbol.setContent(out.toByteArray());
            symbol.setId(characterId);
            entityManager.persist(symbol);

            character.setSymbol(symbol);

            final CharacterEntity character2 = new CharacterEntity();
            character2.setId(UUID.randomUUID().toString());
            character2.setName("fry");
            character2.setCatchphrase("I'm walking on sunshine ! ");

            entityManager.persist(character2);

            character.setLinkedEntities(Collections.singleton((AbstractBlossomEntity) character2));

            entityManager.persist(character);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    @Test(expected = javax.persistence.EntityExistsException.class)
    public void should_fail_id_unicity() throws IOException {
        final EntityManagerFactory createEntityManagerFactory = Persistence.createEntityManagerFactory("BlossomTestLocal");
        final EntityManager entityManager = createEntityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            final CharacterEntity character = new CharacterEntity();
            final String characterId = UUID.randomUUID().toString();
            character.setId(characterId);
            character.setName("farnsworth");
            character.setCatchphrase("Ooooh my yes!");

            final CharacterEntity character2 = new CharacterEntity();
            character2.setId(characterId);
            character2.setName("fry");
            character2.setCatchphrase("I'm walking on sunshine ! ");

            entityManager.persist(character);
            entityManager.persist(character2);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }
}
