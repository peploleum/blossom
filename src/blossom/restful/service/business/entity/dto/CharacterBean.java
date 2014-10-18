package blossom.restful.service.business.entity.dto;

import javax.xml.bind.annotation.XmlRootElement;

import blossom.restful.service.geo.dto.Feature;

@XmlRootElement
public class CharacterBean {
    private String id;
    private String name;

    private String catchphrase;
    private int size;
    private Feature geom;

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getCatchphrase() {
        return this.catchphrase;
    }

    public void setCatchphrase(final String catchphrase) {
        this.catchphrase = catchphrase;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public Feature getGeom() {
        return this.geom;
    }

    public void setGeom(final Feature geom) {
        this.geom = geom;
    }

}
