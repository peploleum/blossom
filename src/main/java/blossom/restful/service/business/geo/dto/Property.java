package blossom.restful.service.business.geo.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Property {
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
