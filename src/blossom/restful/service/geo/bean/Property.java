package blossom.restful.service.geo.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Property {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
