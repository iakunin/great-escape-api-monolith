package com.greatescape.api.monolith.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Thematic.class)
public abstract class Thematic_ {

	public static volatile SetAttribute<Thematic, Quest> quests;
	public static volatile SingularAttribute<Thematic, Long> id;
	public static volatile SingularAttribute<Thematic, String> title;
	public static volatile SingularAttribute<Thematic, String> slug;

	public static final String QUESTS = "quests";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String SLUG = "slug";

}

