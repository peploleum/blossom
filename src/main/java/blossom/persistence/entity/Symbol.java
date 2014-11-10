package blossom.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "tech_symbol", schema = "blossom")
public class Symbol {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "size")
    private int size;

    @Column(name = "content")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte content[];

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public byte[] getContent() {
        return this.content;
    }

    public void setContent(final byte[] content) {
        this.content = content;
    }

}
