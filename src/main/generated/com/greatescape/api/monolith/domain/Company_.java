package com.greatescape.api.monolith.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Company.class)
public abstract class Company_ extends com.greatescape.api.monolith.domain.AbstractEntity_ {

	public static volatile SingularAttribute<Company, String> legalName;
	public static volatile SingularAttribute<Company, Integer> commissionInPercents;
	public static volatile SingularAttribute<Company, Integer> discountInPercents;
	public static volatile SingularAttribute<Company, String> title;
	public static volatile SingularAttribute<Company, String> taxpayerNumber;
	public static volatile SingularAttribute<Company, String> slug;

	public static final String LEGAL_NAME = "legalName";
	public static final String COMMISSION_IN_PERCENTS = "commissionInPercents";
	public static final String DISCOUNT_IN_PERCENTS = "discountInPercents";
	public static final String TITLE = "title";
	public static final String TAXPAYER_NUMBER = "taxpayerNumber";
	public static final String SLUG = "slug";

}

