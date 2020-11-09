package com.greatescape.api.monolith.service.mapper;

import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.QuestPhoto;
import com.greatescape.api.monolith.service.dto.QuestPhotoDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-09T19:52:41+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.9 (Oracle Corporation)"
)
@Component
public class QuestPhotoMapperImpl implements QuestPhotoMapper {

    @Autowired
    private QuestMapper questMapper;

    @Override
    public List<QuestPhoto> toEntity(List<QuestPhotoDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<QuestPhoto> list = new ArrayList<QuestPhoto>( dtoList.size() );
        for ( QuestPhotoDTO questPhotoDTO : dtoList ) {
            list.add( toEntity( questPhotoDTO ) );
        }

        return list;
    }

    @Override
    public List<QuestPhotoDTO> toDto(List<QuestPhoto> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<QuestPhotoDTO> list = new ArrayList<QuestPhotoDTO>( entityList.size() );
        for ( QuestPhoto questPhoto : entityList ) {
            list.add( toDto( questPhoto ) );
        }

        return list;
    }

    @Override
    public QuestPhotoDTO toDto(QuestPhoto questPhoto) {
        if ( questPhoto == null ) {
            return null;
        }

        QuestPhotoDTO questPhotoDTO = new QuestPhotoDTO();

        questPhotoDTO.setQuestId( questPhotoQuestId( questPhoto ) );
        questPhotoDTO.setQuestTitle( questPhotoQuestTitle( questPhoto ) );
        questPhotoDTO.setId( questPhoto.getId() );
        questPhotoDTO.setUrl( questPhoto.getUrl() );

        return questPhotoDTO;
    }

    @Override
    public QuestPhoto toEntity(QuestPhotoDTO questPhotoDTO) {
        if ( questPhotoDTO == null ) {
            return null;
        }

        QuestPhoto questPhoto = new QuestPhoto();

        questPhoto.setQuest( questMapper.fromId( questPhotoDTO.getQuestId() ) );
        questPhoto.setId( questPhotoDTO.getId() );
        questPhoto.setUrl( questPhotoDTO.getUrl() );

        return questPhoto;
    }

    private UUID questPhotoQuestId(QuestPhoto questPhoto) {
        if ( questPhoto == null ) {
            return null;
        }
        Quest quest = questPhoto.getQuest();
        if ( quest == null ) {
            return null;
        }
        UUID id = quest.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String questPhotoQuestTitle(QuestPhoto questPhoto) {
        if ( questPhoto == null ) {
            return null;
        }
        Quest quest = questPhoto.getQuest();
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
