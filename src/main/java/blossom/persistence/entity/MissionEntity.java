package blossom.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mission", schema = "blossom")
@NamedQueries({ @NamedQuery(name = "MissionEntity.findAll", query = "SELECT missionentity FROM MissionEntity missionentity"),
    @NamedQuery(name = "MissionEntity.findById", query = "SELECT c FROM MissionEntity c WHERE c.id = :id")})
public class MissionEntity  extends AbstractBlossomEntity
{
    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Mission : [missionId=");
        stringBuilder.append(getId());
        stringBuilder.append("][name=");
        stringBuilder.append(getName());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}