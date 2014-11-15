package blossom.restful.service.business.mission.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Mission
{
	private String name;
	private String id;
	public void setName(final String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}
}