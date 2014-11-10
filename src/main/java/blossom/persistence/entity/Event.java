package blossom.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "event", schema = "blossom")
@PrimaryKeyJoinColumn(name = "id")
public class Event extends AbstractBlossomEntity  {

}
