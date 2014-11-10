package blossom.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "equipment", schema = "blossom")
@PrimaryKeyJoinColumn(name = "id")
public class Equipment extends AbstractBlossomEntity {

}
