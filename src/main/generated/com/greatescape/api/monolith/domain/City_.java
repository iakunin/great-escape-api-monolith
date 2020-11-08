package com.greatescape.api.monolith.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(City.class)
public abstract class City_ {

	public static volatile SingularAttribute<City, String> timezone;
	public static volatile SingularAttribute<City, Long> id;
	public static volatile SingularAttribute<City, String> title;
	public static volatile SingularAttribute<City, String> slug;

	public static final String TIMEZONE = "timezone";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String SLUG = "slug";

}

