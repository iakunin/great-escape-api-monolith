package com.greatescape.api.monolith.service.mapper;

import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.service.dto.QuestIntegrationSettingDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-09T16:48:55+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.8 (JetBrains s.r.o.)"
)
@Component
public class QuestIntegrationSettingMapperImpl implements QuestIntegrationSettingMapper {

    @Autowired
    private QuestMapper questMapper;

    @Override
    public List<QuestIntegrationSetting> toEntity(List<QuestIntegrationSettingDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<QuestIntegrationSetting> list = new ArrayList<QuestIntegrationSetting>( dtoList.size() );
        for ( QuestIntegrationSettingDTO questIntegrationSettingDTO : dtoList ) {
            list.add( toEntity( questIntegrationSettingDTO ) );
        }

        return list;
    }

    @Override
    public List<QuestIntegrationSettingDTO> toDto(List<QuestIntegrationSetting> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<QuestIntegrationSettingDTO> list = new ArrayList<QuestIntegrationSettingDTO>( entityList.size() );
        for ( QuestIntegrationSetting questIntegrationSetting : entityList ) {
            list.add( toDto( questIntegrationSetting ) );
        }

        return list;
    }

    @Override
    public QuestIntegrationSettingDTO toDto(QuestIntegrationSetting questIntegrationSetting) {
        if ( questIntegrationSetting == null ) {
            return null;
        }

        QuestIntegrationSettingDTO questIntegrationSettingDTO = new QuestIntegrationSettingDTO();

        questIntegrationSettingDTO.setQuestId( questIntegrationSettingQuestId( questIntegrationSetting ) );
        questIntegrationSettingDTO.setQuestTitle( questIntegrationSettingQuestTitle( questIntegrationSetting ) );
        questIntegrationSettingDTO.setId( questIntegrationSetting.getId() );
        questIntegrationSettingDTO.setType( questIntegrationSetting.getType() );
        questIntegrationSettingDTO.setSettings( questIntegrationSetting.getSettings() );

        return questIntegrationSettingDTO;
    }

    @Override
    public QuestIntegrationSetting toEntity(QuestIntegrationSettingDTO questIntegrationSettingDTO) {
        if ( questIntegrationSettingDTO == null ) {
            return null;
        }

        QuestIntegrationSetting questIntegrationSetting = new QuestIntegrationSetting();

        questIntegrationSetting.setQuest( questMapper.fromId( questIntegrationSettingDTO.getQuestId() ) );
        questIntegrationSetting.setId( questIntegrationSettingDTO.getId() );
        questIntegrationSetting.setType( questIntegrationSettingDTO.getType() );
        questIntegrationSetting.setSettings( questIntegrationSettingDTO.getSettings() );

        return questIntegrationSetting;
    }

    private Long questIntegrationSettingQuestId(QuestIntegrationSetting questIntegrationSetting) {
        if ( questIntegrationSetting == null ) {
            return null;
        }
        Quest quest = questIntegrationSetting.getQuest();
        if ( quest == null ) {
            return null;
        }
        Long id = quest.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String questIntegrationSettingQuestTitle(QuestIntegrationSetting questIntegrationSetting) {
        if ( questIntegrationSetting == null ) {
            return null;
        }
        Quest quest = questIntegrationSetting.getQuest();
        if ( quest == null ) {
            return null;
        }
        String title = quest.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }
}
