package blossom.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "character", schema = "blossom")
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

}
