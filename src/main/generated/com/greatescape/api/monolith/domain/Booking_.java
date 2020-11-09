package com.greatescape.api.monolith.domain;

import com.greatescape.api.monolith.domain.enumeration.BookingStatus;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Booking.class)
public abstract class Booking_ extends com.greatescape.api.monolith.domain.AbstractEntity_ {

	public static volatile SingularAttribute<Booking, Integer> commissionInPercents;
	public static volatile SingularAttribute<Booking, Integer> price;
	public static volatile SingularAttribute<Booking, Integer> discountInPercents;
	public static volatile SingularAttribute<Booking, Slot> slot;
	public static volatile SingularAttribute<Booking, Quest> quest;
	public static volatile SingularAttribute<Booking, BookingStatus> status;
	public static volatile SingularAttribute<Booking, Player> player;

	public static final String COMMISSION_IN_PERCENTS = "commissionInPercents";
	public static final String PRICE = "price";
	public static final String DISCOUNT_IN_PERCENTS = "discountInPercents";
	public static final String SLOT = "slot";
	public static final String QUEST = "quest";
	public static final String STATUS = "status";
	public static final String PLAYER = "player";

}

