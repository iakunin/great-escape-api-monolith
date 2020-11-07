package com.greatescape.backend.service.mapper;


import com.greatescape.backend.domain.QuestIntegrationSetting;
import com.greatescape.backend.service.dto.QuestIntegrationSettingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link QuestIntegrationSetting} and its DTO {@link QuestIntegrationSettingDTO}.
 */
@Mapper(componentModel = "spring", uses = {QuestMapper.class})
public interface QuestIntegrationSettingMapper extends EntityMapper<QuestIntegrationSettingDTO, QuestIntegrationSetting> {

    @Mapping(source = "quest.id", target = "questId")
    @Mapping(source = "quest.title", target = "questTitle")
    QuestIntegrationSettingDTO toDto(QuestIntegrationSetting questIntegrationSetting);

    @Mapping(source = "questId", target = "quest")
    QuestIntegrationSetting toEntity(QuestIntegrationSettingDTO questIntegrationSettingDTO);

    default QuestIntegrationSetting fromId(Long id) {
        if (id == null) {
            return null;
        }
        QuestIntegrationSetting questIntegrationSetting = new QuestIntegrationSetting();
        questIntegrationSetting.setId(id);
        return questIntegrationSetting;
    }
}
