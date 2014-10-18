package blossom.restful.service.business.entity.transfer;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import blossom.exception.TopLevelBlossomException;
import blossom.persistence.EntityManagerFactorySingleton;
import blossom.persistence.entity.CharacterEntity;
import blossom.persistence.location.Location;
import blossom.restful.service.business.entity.dto.CharacterBean;
import blossom.restful.service.geo.dto.Feature;
import blossom.util.geo.GeoUtils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class CharacterTransfer {

    private static final Logger LOGGER = Logger.getLogger(CharacterTransfer.class.getName());

    public CharacterTransfer() {

    }

    public void createCharacter(final CharacterBean characterBean) throws TopLevelBlossomException {
        final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            try {
                final CharacterEntity characterEntity = new CharacterEntity();
                characterEntity.setName(characterBean.getName());
                characterEntity.setId(characterBean.getId());
                characterEntity.setCatchphrase(characterBean.getCatchphrase());
                characterEntity.setSize(characterBean.getSize());
                entityManager.persist(characterEntity);

                final Feature geom = characterBean.getGeom();
                if (geom != null) {
                    // assume CRS is 4326 for now
                    // let us say that we cannot have multiple features for any given
                    // business
                    // object atm
                    final Location location = new Location();
                    location.setId(characterBean.getId());
                    final Geometry geometry = GeoUtils.wktToGeometry(GeoUtils.fromFeature(geom));
                    geometry.setSRID(4326);
                    location.setLocation((Point) geometry);
                    entityManager.persist(location);
                }
                entityManager.getTransaction().commit();
            } catch (final Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                throw new TopLevelBlossomException(e, "Failed to create Character in database.");
            }
        } finally {
            entityManager.close();
        }
    }

    public void deleteCharacter(final String id) throws TopLevelBlossomException {
        final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            try {
                final Query namedQuery = entityManager.createNamedQuery("CharacterEntity.findById");
                namedQuery.setParameter("id", id);
                final CharacterEntity result = (CharacterEntity) namedQuery.getSingleResult();
                LOGGER.log(Level.INFO, "Merging " + result.toString());
                entityManager.merge(result);
                LOGGER.log(Level.INFO, "Removing " + result.toString());
                entityManager.remove(result);
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

    public CharacterBean getCharcterById(final String id) {
        final CharacterBean characterBean = new CharacterBean();
        return characterBean;
    }

    public CharacterBean updateCharacter(final CharacterBean characterBean) {
        final CharacterBean newCharacterBean = new CharacterBean();
        return newCharacterBean;
    }
}
