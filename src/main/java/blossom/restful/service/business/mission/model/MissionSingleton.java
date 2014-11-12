package blossom.restful.service.business.mission.model;

import blossom.restful.service.business.mission.dto.Mission;

public class MissionSingleton {

	public static MissionSingleton INSTANCE = null;
	private Mission mission;
	
	private MissionSingleton()
	{
		this.mission = initMission();
		if(this.mission==null)
			this.mission = new Mission();
	}
	
	public static MissionSingleton getInstance()
	{
		if(INSTANCE == null)
			INSTANCE = new MissionSingleton();
		return INSTANCE;
	}
	
	private Mission initMission()
	{
		final Mission mission = new Mission();
		return mission;
	}
	
	public Mission getMission()
	{
		return this.mission;
	}
	
	public void setMissionName(final String name)
	{
		this.mission.setName(name);
	}
}
