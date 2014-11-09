package blossom.restful.service.business.graph.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NodeItem {
    private String id;
    private String name;
    private int size;
    private String catchphrase;

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

    public int getSize() {
        return this.size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public String getCatchphrase() {
        return this.catchphrase;
    }

    public void setCatchphrase(final String catchphrase) {
        this.catchphrase = catchphrase;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof NodeItem) {
            return ((NodeItem) obj).getId().equals(this.id);
        } else {
            return false;
        }

    }

}
