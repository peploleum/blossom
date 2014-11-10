package blossom.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "character", schema = "blossom")
@NamedQueries({ @NamedQuery(name = "CharacterEntity.findAll", query = "SELECT characterentity FROM CharacterEntity characterentity"),
        @NamedQuery(name = "CharacterEntity.countByName", query = "SELECT count(c)  FROM CharacterEntity c WHERE c.name = :name"),
        @NamedQuery(name = "CharacterEntity.findNames", query = "SELECT name  FROM CharacterEntity name"),
        @NamedQuery(name = "CharacterEntity.findById", query = "SELECT c FROM CharacterEntity c WHERE c.id = :id") })
@PrimaryKeyJoinColumn(name = "id")
public class CharacterEntity extends AbstractBlossomEntity {

    @Column(name = "catchphrase")
    private String catchphrase;

    public CharacterEntity() {
    }

    public String getCatchphrase() {
        return this.catchphrase;
    }

    public void setCatchphrase(final String catchphrase) {
        this.catchphrase = catchphrase;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CharacterEntity : [id=");
        stringBuilder.append(getId());
        stringBuilder.append("][name=");
        stringBuilder.append(getName());
        stringBuilder.append("][catchphrase=");
        stringBuilder.append(this.catchphrase);
        stringBuilder.append("][symbol=");
        stringBuilder.append(getSymbol() != null ? getSymbol().getId() : null);
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

}
