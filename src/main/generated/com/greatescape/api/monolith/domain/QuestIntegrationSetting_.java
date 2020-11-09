package com.greatescape.api.monolith.domain;

import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(QuestIntegrationSetting.class)
public abstract class QuestIntegrationSetting_ extends com.greatescape.api.monolith.domain.AbstractEntity_ {

	public static volatile SingularAttribute<QuestIntegrationSetting, String> settings;
	public static volatile SingularAttribute<QuestIntegrationSetting, QuestIntegrationType> type;
	public static volatile SingularAttribute<QuestIntegrationSetting, Quest> quest;

	public static final String SETTINGS = "settings";
	public static final String TYPE = "type";
	public static final String QUEST = "quest";

}

