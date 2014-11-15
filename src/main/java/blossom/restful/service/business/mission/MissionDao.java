package blossom.restful.service.business.mission;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import blossom.exception.TopLevelBlossomException;
import blossom.persistence.EntityManagerFactorySingleton;
import blossom.persistence.entity.MissionEntity;
import blossom.restful.service.business.mission.dto.Mission;

/**
 * Awesome mission tool
 * @author Darkavus
 *	
 */
public class MissionDao {
	private static final Logger LOGGER = Logger.getLogger(MissionDao.class.getName());

	public MissionDao()
	{

	}

	public void setMission(final Mission mission) throws TopLevelBlossomException
	{
		final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
		try
		{
			entityManager.getTransaction().begin();
			try {
				final MissionEntity missionEntity = new MissionEntity();
				missionEntity.setName(mission.getName());
				missionEntity.setId(mission.getId());
				entityManager.persist(missionEntity);
				entityManager.getTransaction().commit();
			} catch (final Exception e) {
				if (entityManager.getTransaction().isActive()) {
					entityManager.getTransaction().rollback();
				}
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
				throw new TopLevelBlossomException(e, "Failed to remove Nodes from database.");
			}
		}
		finally {
			entityManager.close();
		}
	}

	public final List<Mission> getMissions() throws TopLevelBlossomException
	{
		final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
		try
		{
			final Query allMissions = entityManager.createNamedQuery("MissionEntity.findAll");
			try 
			{
				@SuppressWarnings("unchecked")
				final List<MissionEntity> resultList = allMissions.getResultList();
				final List<Mission> missions = new ArrayList<Mission>();
				for(final MissionEntity missionEntity:resultList)
				{
					final Mission mission = new Mission();
					mission.setId(missionEntity.getId());
					mission.setName(missionEntity.getName());
					missions.add(mission);
				}
				return missions;
			}
			catch (final Exception e)
			{
				if (entityManager.getTransaction().isActive())
				{
					entityManager.getTransaction().rollback();
				}
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
				throw new TopLevelBlossomException(e, "Failed to remove Nodes from database.");
			}
		}
		finally
		{
			entityManager.close();
		}
	}

	public Mission getMissionById(String id) throws TopLevelBlossomException 
	{
		final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            try {
                final Query namedQuery = entityManager.createNamedQuery("MissionEntity.findById");
                namedQuery.setParameter("id", id.trim());
                final MissionEntity result = (MissionEntity) namedQuery.getSingleResult();
                LOGGER.log(Level.INFO, "Found " + result.toString());
                final Mission mission = new Mission();
                mission.setId(result.getId());
                mission.setName(result.getName());
                return mission;
            } catch (final Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                throw new TopLevelBlossomException(e, "Failed to fetch item from database.");
            }
        } finally {
            entityManager.close();
        }
	}
}