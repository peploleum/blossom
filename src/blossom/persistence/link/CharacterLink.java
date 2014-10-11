package blossom.persistence.link;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "link", schema = "blossom")
@NamedQueries({ @NamedQuery(name = "CharacterLink.findAll", query = "SELECT cl FROM CharacterLink cl"),
    @NamedQuery(name = "CharacterLink.findById", query = "SELECT cl FROM CharacterLink cl WHERE (cl.source = :idsource OR cl.dest = :iddest)") })
public class CharacterLink {
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
        stringBuilder.append("CharacterLink : [source=");
        stringBuilder.append(this.source);
        stringBuilder.append("][dest=");
        stringBuilder.append(this.dest);
        stringBuilder.append("][name=");
        stringBuilder.append(this.name);
        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
