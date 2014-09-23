package blossom.restful.geo.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Coordinate {

    private double coordValue;

    public double getValue() {
        return this.coordValue;
    }

    public void setValue(final double value) {
        this.coordValue = value;
    }

}
