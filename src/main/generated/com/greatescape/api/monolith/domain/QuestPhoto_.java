package com.greatescape.api.monolith.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(QuestPhoto.class)
public abstract class QuestPhoto_ {

	public static volatile SingularAttribute<QuestPhoto, Long> id;
	public static volatile SingularAttribute<QuestPhoto, Quest> quest;
	public static volatile SingularAttribute<QuestPhoto, String> url;

	public static final String ID = "id";
	public static final String QUEST = "quest";
	public static final String URL = "url";

}

