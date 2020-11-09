package com.greatescape.api.monolith.service.mapper;


import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.service.dto.QuestIntegrationSettingDTO;
import java.util.UUID;
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
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    QuestIntegrationSetting toEntity(QuestIntegrationSettingDTO questIntegrationSettingDTO);

    default QuestIntegrationSetting fromId(UUID id) {
        if (id == null) {
            return null;
        }
        QuestIntegrationSetting questIntegrationSetting = new QuestIntegrationSetting();
        questIntegrationSetting.setId(id);
        return questIntegrationSetting;
    }
}
