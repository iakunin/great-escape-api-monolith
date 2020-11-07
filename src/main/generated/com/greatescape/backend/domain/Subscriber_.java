package com.greatescape.backend.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Subscriber.class)
public abstract class Subscriber_ {

	public static volatile SingularAttribute<Subscriber, String> name;
	public static volatile SingularAttribute<Subscriber, Long> id;
	public static volatile SingularAttribute<Subscriber, String> email;

	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String EMAIL = "email";

}

