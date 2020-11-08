package com.greatescape.api.monolith.service.mapper;


import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.service.dto.QuestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Quest} and its DTO {@link QuestDTO}.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class, CompanyMapper.class, ThematicMapper.class})
public interface QuestMapper extends EntityMapper<QuestDTO, Quest> {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.address", target = "locationAddress")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.title", target = "companyTitle")
    QuestDTO toDto(Quest quest);

    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "companyId", target = "company")
    @Mapping(target = "removeThematic", ignore = true)
    Quest toEntity(QuestDTO questDTO);

    default Quest fromId(Long id) {
        if (id == null) {
            return null;
        }
        Quest quest = new Quest();
        quest.setId(id);
        return quest;
    }
}
