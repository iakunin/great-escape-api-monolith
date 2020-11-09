package com.greatescape.api.monolith.domain;

import java.time.Instant;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Slot.class)
public abstract class Slot_ extends com.greatescape.api.monolith.domain.AbstractEntity_ {

	public static volatile SingularAttribute<Slot, Integer> commissionInPercents;
	public static volatile SingularAttribute<Slot, Boolean> isAvailable;
	public static volatile SingularAttribute<Slot, Integer> price;
	public static volatile SingularAttribute<Slot, ZonedDateTime> dateTimeWithTimeZone;
	public static volatile SingularAttribute<Slot, Integer> discountInPercents;
	public static volatile SingularAttribute<Slot, String> externalId;
	public static volatile SingularAttribute<Slot, String> externalState;
	public static volatile SingularAttribute<Slot, Instant> dateTimeLocal;
	public static volatile SingularAttribute<Slot, Quest> quest;

	public static final String COMMISSION_IN_PERCENTS = "commissionInPercents";
	public static final String IS_AVAILABLE = "isAvailable";
	public static final String PRICE = "price";
	public static final String DATE_TIME_WITH_TIME_ZONE = "dateTimeWithTimeZone";
	public static final String DISCOUNT_IN_PERCENTS = "discountInPercents";
	public static final String EXTERNAL_ID = "externalId";
	public static final String EXTERNAL_STATE = "externalState";
	public static final String DATE_TIME_LOCAL = "dateTimeLocal";
	public static final String QUEST = "quest";

}

