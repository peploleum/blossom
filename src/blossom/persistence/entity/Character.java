package blossom.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "character", schema = "blossom")
public class Character {
    @Id
    @Column(name = "id")
    private String id;
}
