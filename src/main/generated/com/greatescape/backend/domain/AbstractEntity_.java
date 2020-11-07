package com.greatescape.backend.domain;

import java.time.ZonedDateTime;
import java.util.UUID;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AbstractEntity.class)
public abstract class AbstractEntity_ {

	public static volatile SingularAttribute<AbstractEntity, ZonedDateTime> createdAt;
	public static volatile SingularAttribute<AbstractEntity, UUID> id;
	public static volatile SingularAttribute<AbstractEntity, ZonedDateTime> updatedAt;

	public static final String CREATED_AT = "createdAt";
	public static final String ID = "id";
	public static final String UPDATED_AT = "updatedAt";

}

