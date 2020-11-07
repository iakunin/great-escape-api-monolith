package com.greatescape.backend.domain;

import com.greatescape.backend.domain.enumeration.QuestIntegrationType;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(QuestIntegrationSetting.class)
public abstract class QuestIntegrationSetting_ {

	public static volatile SingularAttribute<QuestIntegrationSetting, String> settings;
	public static volatile SingularAttribute<QuestIntegrationSetting, Long> id;
	public static volatile SingularAttribute<QuestIntegrationSetting, QuestIntegrationType> type;
	public static volatile SingularAttribute<QuestIntegrationSetting, Quest> quest;

	public static final String SETTINGS = "settings";
	public static final String ID = "id";
	public static final String TYPE = "type";
	public static final String QUEST = "quest";

}

