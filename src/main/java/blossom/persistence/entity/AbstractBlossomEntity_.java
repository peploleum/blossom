package blossom.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AbstractBlossomEntity.class)
public abstract class AbstractBlossomEntity_ {

	public static volatile SingularAttribute<AbstractBlossomEntity, String> id;
	public static volatile SingularAttribute<AbstractBlossomEntity, Symbol> symbol;
	public static volatile SetAttribute<AbstractBlossomEntity, AbstractBlossomEntity> linkedEntities;
	public static volatile SingularAttribute<AbstractBlossomEntity, String> name;

}

