package blossom.restful.service.tech.table.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TableRow {
    private String id;
    private String name;
    private String catchphrase;
    private int size;

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
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

}
