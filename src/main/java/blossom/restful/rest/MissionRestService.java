package blossom.restful.rest;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import blossom.exception.TopLevelBlossomException;
import blossom.restful.service.business.mission.MissionDao;
import blossom.restful.service.business.mission.dto.Mission;
import blossom.restful.service.tech.table.TableDao;
import blossom.restful.service.tech.table.dto.TableRowCollection;

@Path("/mission")
public class MissionRestService
{
	private static final Logger LOGGER = Logger.getLogger(MissionRestService.class.getName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getMissions")
	public TableRowCollection getMissions() throws TopLevelBlossomException 
	{
		LOGGER.info("getting mission model");
		final TableDao missionTransfer = new TableDao();
		return missionTransfer.getTableRows();
	}

	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("{id}")
	public Mission getMissionById(@javax.ws.rs.PathParam("id") final String id) throws TopLevelBlossomException {
		final MissionDao missionTransfer = new MissionDao();
		return missionTransfer.getMissionById(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/addMission")
	public void addMission(final Mission mission) throws TopLevelBlossomException
	{
		LOGGER.info("adding mission");
		final MissionDao missionDao = new MissionDao();
		missionDao.setMission(mission);
	}
}
