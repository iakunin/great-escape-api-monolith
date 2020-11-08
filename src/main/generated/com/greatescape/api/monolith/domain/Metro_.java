package com.greatescape.api.monolith.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Metro.class)
public abstract class Metro_ extends com.greatescape.api.monolith.domain.AbstractEntity_ {

	public static volatile SetAttribute<Metro, Location> locations;
	public static volatile SingularAttribute<Metro, String> title;
	public static volatile SingularAttribute<Metro, String> slug;

	public static final String LOCATIONS = "locations";
	public static final String TITLE = "title";
	public static final String SLUG = "slug";

}

