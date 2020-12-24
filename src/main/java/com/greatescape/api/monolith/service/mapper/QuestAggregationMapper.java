package com.greatescape.api.monolith.service.mapper;

import com.greatescape.api.monolith.domain.QuestAggregation;
import com.greatescape.api.monolith.service.dto.QuestAggregationDTO;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link QuestAggregation} and its DTO {@link QuestAggregationDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        LocationMapper.class,
        CompanyMapper.class,
        ThematicMapper.class,
        MetroMapper.class,
        QuestPhotoMapper.class,
})
public interface QuestAggregationMapper extends EntityMapper<QuestAggregationDTO, QuestAggregation> {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.address", target = "locationAddress")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.title", target = "companyTitle")
    @Mapping(source = "location.metros", target = "metros")
    QuestAggregationDTO toDto(QuestAggregation quest);

    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "companyId", target = "company")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    QuestAggregation toEntity(QuestAggregationDTO questDTO);

    default QuestAggregation fromId(UUID id) {
        if (id == null) {
            return null;
        }
        QuestAggregation quest = new QuestAggregation();
        quest.setId(id);
        return quest;
    }
}
