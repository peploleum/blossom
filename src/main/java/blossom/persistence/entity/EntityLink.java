package blossom.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "link", schema = "blossom")
@NamedQueries({ @NamedQuery(name = "EntityLink.findAll", query = "SELECT el FROM EntityLink el"),
    @NamedQuery(name = "EntityLink.findById", query = "SELECT el FROM EntityLink el WHERE (el.source = :idsource OR el.dest = :iddest)") })
public class EntityLink {
    @Id
    @Column(name = "source")
    private String source;
    @Column(name = "dest")
    private String dest;
    @Column(name = "name")
    private String name;

    public String getSource() {
        return this.source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getDest() {
        return this.dest;
    }

    public void setDest(final String dest) {
        this.dest = dest;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("EntityLink : [source=");
        stringBuilder.append(this.source);
        stringBuilder.append("][dest=");
        stringBuilder.append(this.dest);
        stringBuilder.append("][name=");
        stringBuilder.append(this.name);
        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
