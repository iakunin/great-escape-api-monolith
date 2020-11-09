package com.greatescape.api.monolith.domain;

import com.greatescape.api.monolith.domain.enumeration.Gender;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Player.class)
public abstract class Player_ extends com.greatescape.api.monolith.domain.AbstractEntity_ {

	public static volatile SingularAttribute<Player, LocalDate> birthday;
	public static volatile SingularAttribute<Player, Gender> gender;
	public static volatile SingularAttribute<Player, User> internalUser;
	public static volatile SingularAttribute<Player, String> phone;
	public static volatile SingularAttribute<Player, Boolean> subscriptionAllowed;
	public static volatile SingularAttribute<Player, String> name;
	public static volatile SingularAttribute<Player, Company> company;
	public static volatile SingularAttribute<Player, String> email;

	public static final String BIRTHDAY = "birthday";
	public static final String GENDER = "gender";
	public static final String INTERNAL_USER = "internalUser";
	public static final String PHONE = "phone";
	public static final String SUBSCRIPTION_ALLOWED = "subscriptionAllowed";
	public static final String NAME = "name";
	public static final String COMPANY = "company";
	public static final String EMAIL = "email";

}

