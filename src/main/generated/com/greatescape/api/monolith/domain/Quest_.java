package com.greatescape.api.monolith.domain;

import com.greatescape.api.monolith.domain.enumeration.FearLevel;
import com.greatescape.api.monolith.domain.enumeration.QuestComplexity;
import com.greatescape.api.monolith.domain.enumeration.QuestType;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Quest.class)
public abstract class Quest_ {

	public static volatile SingularAttribute<Quest, QuestComplexity> complexity;
	public static volatile SingularAttribute<Quest, Integer> playersMaxCount;
	public static volatile SingularAttribute<Quest, Integer> durationInMinutes;
	public static volatile SingularAttribute<Quest, String> description;
	public static volatile SingularAttribute<Quest, FearLevel> fearLevel;
	public static volatile SingularAttribute<Quest, String> title;
	public static volatile SingularAttribute<Quest, QuestType> type;
	public static volatile SingularAttribute<Quest, Integer> playersMinCount;
	public static volatile SetAttribute<Quest, Thematic> thematics;
	public static volatile SingularAttribute<Quest, Location> location;
	public static volatile SingularAttribute<Quest, Company> company;
	public static volatile SingularAttribute<Quest, Long> id;
	public static volatile SingularAttribute<Quest, String> slug;

	public static final String COMPLEXITY = "complexity";
	public static final String PLAYERS_MAX_COUNT = "playersMaxCount";
	public static final String DURATION_IN_MINUTES = "durationInMinutes";
	public static final String DESCRIPTION = "description";
	public static final String FEAR_LEVEL = "fearLevel";
	public static final String TITLE = "title";
	public static final String TYPE = "type";
	public static final String PLAYERS_MIN_COUNT = "playersMinCount";
	public static final String THEMATICS = "thematics";
	public static final String LOCATION = "location";
	public static final String COMPANY = "company";
	public static final String ID = "id";
	public static final String SLUG = "slug";

}

