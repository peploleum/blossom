package blossom.restful.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import blossom.exception.TopLevelBlossomException;
import blossom.persistence.EntityManagerFactorySingleton;
import blossom.persistence.entity.CharacterEntity;
import blossom.restful.stat.Stat;
import blossom.restful.stat.StatItem;

@Path("/stat")
public class StatRestService {
    private final static Logger LOGGER = Logger.getLogger(StatRestService.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Stat getStat() throws TopLevelBlossomException {
        LOGGER.log(Level.FINE, "Executing getStat for the whole database");
        final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
        final Stat stat = new Stat();
        try {
            final List<StatItem> dataset = new ArrayList<StatItem>();
            final CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            // Query for a List of objects.
            final CriteriaQuery<Object> cq = cb.createQuery();
            final Root<CharacterEntity> c = cq.from(CharacterEntity.class);
            cq.select(c.get("name"));
            cq.groupBy(c.get("name"));
            cb.asc(c.get("name"));
            // cq.where(cb.equal(c.get("name"), cb.parameter(String.class,
            // "name")));
            final Query query = entityManager.createQuery(cq);
            try {
                @SuppressWarnings("unchecked")
                final List<String> resultList = query.getResultList();
                for (final String result : resultList) {
                    LOGGER.log(Level.INFO, "Computing stat for name: " + result);
                    final Query numberQuery = entityManager.createNamedQuery("CharacterEntity.countByName");
                    numberQuery.setParameter("name", result);
                    Number statResult = (Number) numberQuery.getSingleResult();
                    LOGGER.log(Level.INFO, "Stat for name: " + result + "=" + statResult.intValue());
                    StatItem statItem = new StatItem();
                    statItem.setValue(statResult.intValue());
                    statItem.setValued(result);
                    dataset.add(statItem);
                }
                stat.setDataset(dataset);

            } catch (final Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                throw new TopLevelBlossomException(e, "Failed to remove Nodes from database.");
            }
        } finally {
            entityManager.close();
        }
        return stat;
    }
}
