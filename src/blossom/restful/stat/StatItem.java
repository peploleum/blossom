package blossom.restful.stat;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StatItem {
    private String valued;
    private int value;

    public String getValued() {
        return this.valued;
    }

    public void setValued(final String valued) {
        this.valued = valued;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(final int value) {
        this.value = value;
    }

}
