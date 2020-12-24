package com.greatescape.api.monolith.service.mapper;


import com.greatescape.api.monolith.domain.QuestPhoto;
import com.greatescape.api.monolith.service.dto.QuestPhotoDTO;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link QuestPhoto} and its DTO {@link QuestPhotoDTO}.
 */
@Mapper(componentModel = "spring", uses = {QuestMapper.class})
public interface QuestPhotoMapper extends EntityMapper<QuestPhotoDTO, QuestPhoto> {

    @Mapping(source = "quest.id", target = "questId")
    QuestPhotoDTO toDto(QuestPhoto questPhoto);

    @Mapping(source = "questId", target = "quest")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    QuestPhoto toEntity(QuestPhotoDTO questPhotoDTO);

    default QuestPhoto fromId(UUID id) {
        if (id == null) {
            return null;
        }
        QuestPhoto questPhoto = new QuestPhoto();
        questPhoto.setId(id);
        return questPhoto;
    }
}
