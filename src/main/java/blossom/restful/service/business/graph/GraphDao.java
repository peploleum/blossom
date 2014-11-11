package blossom.restful.service.business.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import blossom.exception.TopLevelBlossomException;
import blossom.persistence.EntityManagerFactorySingleton;
import blossom.persistence.entity.CharacterEntity;
import blossom.persistence.entity.CharacterLink;
import blossom.restful.service.business.graph.dto.Graph;
import blossom.restful.service.business.graph.dto.GraphNodeIdCollection;
import blossom.restful.service.business.graph.dto.LinkItem;
import blossom.restful.service.business.graph.dto.NodeItem;
import blossom.restful.service.business.graph.model.GraphSingleton;

/**
 * Handles Graph model interactions with persistence layer
 *
 * @author peploleum
 *
 */
public class GraphDao {

    private static final Logger LOGGER = Logger.getLogger(GraphDao.class.getName());

    /**
     * Handles Graph model interactions with persistence layer
     */
    public GraphDao() {

    }

    /**
     * Get the graph model
     *
     * @return {@link Graph} model
     * @throws TopLevelBlossomException
     *             when persistence layer fails
     */
    public Graph getGraph() throws TopLevelBlossomException {
        final GraphSingleton gs = GraphSingleton.getInstance();
        final Graph graph = gs.getGraph();

        final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
        try {
            // first we fetch nodes
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
                    //default size for now, until we have proper symbol management.
                    nodeItem.setSize(result.getSymbol() != null ? result.getSymbol().getSize() : 70);
                    nodeList.add(nodeItem);
                }
                graph.setNodes(nodeList);

                // then we fetch links
                final Query linksQuery = entityManager.createNamedQuery("CharacterLink.findAll");
                @SuppressWarnings("unchecked")
                final List<CharacterLink> links = linksQuery.getResultList();
                final List<LinkItem> linkItems = new ArrayList<LinkItem>();
                for (final CharacterLink characterLink : links) {
                    final LinkItem li = new LinkItem();
                    li.setSource(gs.getNodeIndexByNodeId(characterLink.getSource()));
                    li.setTarget(gs.getNodeIndexByNodeId(characterLink.getDest()));
                    linkItems.add(li);
                }
                graph.setLinks(linkItems);

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

    /**
     * Add a node to the graph model
     *
     * @param node
     *            {@link NodeItem}
     * @throws TopLevelBlossomException
     *             when persistence layer fails
     */
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
                // characterEntity.setSize(node.getSize());
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

    /**
     * Adds a link the the graph model
     *
     * @param links
     *            {@link List} of {@link LinkItem} to add
     * @throws TopLevelBlossomException
     *             when persistence layer fails
     */
    public void addLink(final List<LinkItem> links) throws TopLevelBlossomException {
        final GraphSingleton gs = GraphSingleton.getInstance();
        final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            try {
                for (final LinkItem linkItem : links) {
                    final CharacterLink cl = new CharacterLink();
                    cl.setDest(gs.getGraph().getNodes().get(linkItem.getTarget()).getId());
                    cl.setSource(gs.getGraph().getNodes().get(linkItem.getSource()).getId());
                    entityManager.persist(cl);
                    entityManager.getTransaction().commit();
                    gs.addLink(linkItem);
                }
            } catch (final Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                throw new TopLevelBlossomException(e, "Failed to add link in database.");
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Remove node by unique identifier: fetches the node in the model, removes it and all the links
     * to or from it
     *
     * @param nodeId
     *            {@link String} unique id of the node
     * @throws TopLevelBlossomException
     *             when persistence layer fails
     */
    public void removeNode(final String nodeId) throws TopLevelBlossomException {
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

    /**
     * Removes a single node from the graph model and from the database
     *
     * @param nodeId
     *            unique identifier of the node to remove
     * @throws TopLevelBlossomException
     *             if persistence layer fails
     */
    public void removeSingleNode(final String nodeId) throws TopLevelBlossomException {
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

    /**
     * Remove node by ids. Given a node ids collection, handles removal from the graph model and
     * from the database
     *
     * @param ids
     *            {@link GraphNodeIdCollection} the collection of node ids to remove
     * @throws TopLevelBlossomException
     *             when persistence layer fails
     */
    public void removeNodes(final GraphNodeIdCollection ids) throws TopLevelBlossomException {
        final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            try {
                for (final String nodeId : ids.getIds()) {
                    LOGGER.info("removing node " + nodeId);
                    final Query namedQuery = entityManager.createNamedQuery("CharacterEntity.findById");
                    namedQuery.setParameter("id", nodeId);
                    final CharacterEntity result = (CharacterEntity) namedQuery.getSingleResult();
                    LOGGER.log(Level.INFO, "Merging " + result.toString());
                    entityManager.merge(result);
                    LOGGER.log(Level.INFO, "Removing " + result.toString());
                    entityManager.remove(result);
                    final GraphSingleton gs = GraphSingleton.getInstance();

                    final Query findLinkQuery = entityManager.createNamedQuery("CharacterLink.findById");
                    findLinkQuery.setParameter("idsource", nodeId);
                    findLinkQuery.setParameter("iddest", nodeId);
                    @SuppressWarnings("unchecked")
                    final List<CharacterLink> resultList = findLinkQuery.getResultList();
                    for (final CharacterLink characterLink : resultList) {
                        LOGGER.info("removing link " + characterLink.toString());
                        entityManager.remove(characterLink);
                    }
                    gs.removeLinkById(nodeId);
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
