package blossom.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "organisation", schema = "blossom")
@PrimaryKeyJoinColumn(name = "id")
public class Organisation extends AbstractBlossomEntity {

}
