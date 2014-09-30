package blossom.restful.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import blossom.exception.TopLevelBlossomException;
import blossom.persistence.EntityManagerFactorySingleton;
import blossom.persistence.entity.CharacterEntity;
import blossom.restful.graph.Graph;
import blossom.restful.graph.GraphNodeIdCollection;
import blossom.restful.graph.LinkItem;
import blossom.restful.graph.NodeItem;
import blossom.restful.service.graph.GraphSingleton;
import blossom.restful.stat.GraphStat;
import blossom.restful.stat.GraphStatItem;

@Path("/graph")
public class GraphRestService {

    private static final Logger LOGGER = Logger.getLogger(GraphRestService.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Graph getGraph() throws TopLevelBlossomException {
        // to be able to return a marshalled json object based on a Jaxb bean it
        // seems we need to deploy heavy artillery with glassfish servlet + moxy
        // ...
        final GraphSingleton gs = GraphSingleton.getInstance();
        final Graph graph = gs.getGraph();

        final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
        try {
            final List<NodeItem> nodeList = new ArrayList<NodeItem>();
            final Query nameQuery = entityManager.createNamedQuery("CharacterEntity.findAll");
            try {
                @SuppressWarnings("unchecked")
                final List<CharacterEntity> resultList = nameQuery.getResultList();
                for (final CharacterEntity result : resultList) {
                    final NodeItem nodeItem = new NodeItem();
                    nodeItem.setId(result.getId());
                    nodeItem.setCatchphrase(result.getCatchphrase());
                    nodeItem.setName(result.getName());
                    nodeItem.setSize(result.getSize());
                    nodeList.add(nodeItem);
                }
                graph.setNodes(nodeList);

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
        return graph;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/stat")
    public GraphStat computeStats() throws TopLevelBlossomException {
        try {
            final Graph graph = GraphSingleton.getInstance().getGraph();
            final List<NodeItem> nodes = graph.getNodes();
            final GraphStat graphStat = new GraphStat();
            final List<GraphStatItem> map = new ArrayList<GraphStatItem>();
            graphStat.setMap(map);
            int max = 0;
            for (final NodeItem nodeItem : nodes) {
                final String name = nodeItem.getName();
                final GraphStatItem byName = graphStat.getByName(name);
                if (byName != null) {
                    final Integer stat = byName.getStat();
                    final Integer computedStat = (stat == null) ? Integer.valueOf(1) : Integer.valueOf(stat.intValue() + 1);
                    max = Math.max(computedStat.intValue(), max);
                    byName.setStat(computedStat);
                } else {
                    final GraphStatItem graphStatItem = new GraphStatItem();
                    graphStatItem.setName(name);
                    graphStatItem.setStat(Integer.valueOf(1));
                    graphStat.getMap().add(graphStatItem);
                }
            }
            return graphStat;
        } catch (Exception e) {
            throw new TopLevelBlossomException(e, "Failed to remove Nodes from database.");
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addNode")
    public void addNode(final NodeItem node) throws TopLevelBlossomException {
        final GraphSingleton gs = GraphSingleton.getInstance();

        final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            try {
                final CharacterEntity characterEntity = new CharacterEntity();
                characterEntity.setName(node.getName());
                characterEntity.setId(node.getId());
                characterEntity.setCatchphrase(node.getCatchphrase());
                characterEntity.setSize(node.getSize());
                entityManager.persist(characterEntity);
                entityManager.getTransaction().commit();
                gs.addNode(node);
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

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addLink")
    public void addLink(final List<LinkItem> link) throws TopLevelBlossomException {
        try {
            final GraphSingleton gs = GraphSingleton.getInstance();
            for (final LinkItem linkItem : link) {
                gs.getGraph().getLinks().add(linkItem);
            }
        } catch (Exception e) {
            throw new TopLevelBlossomException(e, "Failed to remove Nodes from database.");
        }
    }

    @POST
    @Path("/persistGraph")
    public void persistGraph(final Graph graph) {
    }

    @DELETE
    @Path("/removeNode")
    public void deleteNode(final String nodeId) throws TopLevelBlossomException {
        final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            try {
                final Query namedQuery = entityManager.createNamedQuery("CharacterEntity.findById");
                namedQuery.setParameter("id", nodeId);
                final CharacterEntity result = (CharacterEntity) namedQuery.getSingleResult();
                LOGGER.log(Level.INFO, "Merging " + result.toString());
                entityManager.merge(result);
                LOGGER.log(Level.INFO, "Removing " + result.toString());
                entityManager.remove(result);
                final GraphSingleton gs = GraphSingleton.getInstance();
                gs.removeNodeById(nodeId);
                entityManager.getTransaction().commit();
            } catch (final Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                throw new TopLevelBlossomException(e, "Failed to remove Node from database.");
            }
        } finally {
            entityManager.close();
        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/removeNodes")
    public void deleteNodes(final GraphNodeIdCollection ids) throws TopLevelBlossomException {
        final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            try {
                for (final String nodeId : ids.getIds()) {
                    final Query namedQuery = entityManager.createNamedQuery("CharacterEntity.findById");
                    namedQuery.setParameter("id", nodeId);
                    final CharacterEntity result = (CharacterEntity) namedQuery.getSingleResult();
                    LOGGER.log(Level.INFO, "Merging " + result.toString());
                    entityManager.merge(result);
                    LOGGER.log(Level.INFO, "Removing " + result.toString());
                    entityManager.remove(result);
                    final GraphSingleton gs = GraphSingleton.getInstance();
                    gs.removeNodeById(nodeId);
                }
                entityManager.getTransaction().commit();
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
    }

}
