package blossom.persistence;

import static org.junit.Assert.assertTrue;

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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import blossom.persistence.entity.AbstractBlossomEntity;
import blossom.persistence.entity.CharacterEntity;
import blossom.persistence.entity.Symbol;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersistenceFullTest {

    @Test
    public void a_purgeTestDatabase() {
        final EntityManagerFactory createEntityManagerFactory = Persistence.createEntityManagerFactory("BlossomTestLocal");
        final EntityManager entityManager = createEntityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            final Query nameQuery = entityManager.createNamedQuery("AbstractBlossomEntity.findAll");
            @SuppressWarnings("unchecked")
            final List<AbstractBlossomEntity> resultList = nameQuery.getResultList();
            for (final AbstractBlossomEntity abstractEntity : resultList) {
                entityManager.remove(abstractEntity);
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
    public void b_shouldLinkEntities() throws IOException {
        final EntityManagerFactory createEntityManagerFactory = Persistence.createEntityManagerFactory("BlossomTestLocal");
        final EntityManager entityManager = createEntityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            final CharacterEntity character = new CharacterEntity();
            final String characterId = "d6228b28-12c2-4bae-8c89-41b6642c379c";
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
            character2.setFirstname("Phillip");
            character2.setLastname("Fry");
            character2.setCatchphrase("I'm walking on sunshine ! ");

            entityManager.persist(character2);

            character.setLinkedEntities(Collections.singleton((AbstractBlossomEntity) character2));

            entityManager.persist(character);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    @Test
    public void c_shouldRemoveEntityAndImpactedLink() throws IOException {
        final EntityManagerFactory createEntityManagerFactory = Persistence.createEntityManagerFactory("BlossomTestLocal");
        final EntityManager entityManager = createEntityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            final CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            // Query for the exact character instance
            final CriteriaQuery<Object> cq = cb.createQuery();
            final Root<AbstractBlossomEntity> c = cq.from(AbstractBlossomEntity.class);
            cq.select(c);
            cq.where(cb.equal(c.get("id"), "d6228b28-12c2-4bae-8c89-41b6642c379c"));
            // cq.where(cb.equal(c.get("lastname"), "Fry"));

            final Query query = entityManager.createQuery(cq);

            final Object singleResult = query.getSingleResult();
            assertTrue(singleResult instanceof CharacterEntity);

            entityManager.remove(singleResult);

            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    @Test(expected = javax.persistence.EntityExistsException.class)
    public void d_should_fail_id_unicity() throws IOException {
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
