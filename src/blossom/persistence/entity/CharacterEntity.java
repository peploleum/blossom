package blossom.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "character", schema = "blossom")
@NamedQueries({ @NamedQuery(name = "CharacterEntity.findAll", query = "SELECT characterentity FROM CharacterEntity characterentity"),
        @NamedQuery(name = "CharacterEntity.findById", query = "SELECT c FROM CharacterEntity c WHERE c.id = :id") })
public class CharacterEntity {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "catchphrase")
    private String catchphrase;
    @Column(name = "size")
    private int size;

    public CharacterEntity() {
    }

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

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CharacterEntity : [id=");
        stringBuilder.append(this.id);
        stringBuilder.append("][name=");
        stringBuilder.append(this.name);
        stringBuilder.append("][catchphrase=");
        stringBuilder.append(this.catchphrase);
        stringBuilder.append("][size=");
        stringBuilder.append(String.valueOf(this.size));
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

}
