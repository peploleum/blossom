package blossom.persistence.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "entity", schema = "blossom")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({ @NamedQuery(name = "AbstractBlossomEntity.findAll", query = "SELECT abstractblossomentity FROM AbstractBlossomEntity abstractblossomentity"),
        @NamedQuery(name = "AbstractBlossomEntity.findById", query = "SELECT c FROM AbstractBlossomEntity c WHERE c.id = :id") })
public abstract class AbstractBlossomEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_symbol")
    private Symbol symbol;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = "blossom", name = "link", joinColumns = @JoinColumn(name = "dest"), inverseJoinColumns = @JoinColumn(name = "source"))
    private Set<AbstractBlossomEntity> linkedEntities;

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

    public Symbol getSymbol() {
        return this.symbol;
    }

    public void setSymbol(final Symbol symbol) {
        this.symbol = symbol;
    }

    public Set<AbstractBlossomEntity> getLinkedEntities() {
        return this.linkedEntities;
    }

    public void setLinkedEntities(final Set<AbstractBlossomEntity> linkedEntities) {
        this.linkedEntities = linkedEntities;
    }

}
