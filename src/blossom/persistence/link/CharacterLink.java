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
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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
