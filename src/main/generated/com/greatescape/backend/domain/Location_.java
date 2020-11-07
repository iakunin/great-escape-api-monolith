package com.greatescape.backend.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Location.class)
public abstract class Location_ {

	public static volatile SingularAttribute<Location, String> address;
	public static volatile SingularAttribute<Location, City> city;
	public static volatile SingularAttribute<Location, Long> id;
	public static volatile SingularAttribute<Location, String> addressExplanation;
	public static volatile SetAttribute<Location, Metro> metros;

	public static final String ADDRESS = "address";
	public static final String CITY = "city";
	public static final String ID = "id";
	public static final String ADDRESS_EXPLANATION = "addressExplanation";
	public static final String METROS = "metros";

}

