package com.greatescape.backend.service.mapper;


import com.greatescape.backend.domain.QuestPhoto;
import com.greatescape.backend.service.dto.QuestPhotoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link QuestPhoto} and its DTO {@link QuestPhotoDTO}.
 */
@Mapper(componentModel = "spring", uses = {QuestMapper.class})
public interface QuestPhotoMapper extends EntityMapper<QuestPhotoDTO, QuestPhoto> {

    @Mapping(source = "quest.id", target = "questId")
    @Mapping(source = "quest.title", target = "questTitle")
    QuestPhotoDTO toDto(QuestPhoto questPhoto);

    @Mapping(source = "questId", target = "quest")
    QuestPhoto toEntity(QuestPhotoDTO questPhotoDTO);

    default QuestPhoto fromId(Long id) {
        if (id == null) {
            return null;
        }
        QuestPhoto questPhoto = new QuestPhoto();
        questPhoto.setId(id);
        return questPhoto;
    }
}
