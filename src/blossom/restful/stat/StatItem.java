package blossom.restful.stat;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StatItem {
    private String valued;
    private int value;

    public String getValued() {
        return valued;
    }

    public void setValued(String valued) {
        this.valued = valued;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
