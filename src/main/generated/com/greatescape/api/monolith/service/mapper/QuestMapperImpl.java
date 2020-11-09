package com.greatescape.api.monolith.service.mapper;

import com.greatescape.api.monolith.domain.Company;
import com.greatescape.api.monolith.domain.Location;
import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.Thematic;
import com.greatescape.api.monolith.service.dto.QuestDTO;
import com.greatescape.api.monolith.service.dto.ThematicDTO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-09T18:35:10+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.9 (Oracle Corporation)"
)
@Component
public class QuestMapperImpl implements QuestMapper {

    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private ThematicMapper thematicMapper;

    @Override
    public List<Quest> toEntity(List<QuestDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Quest> list = new ArrayList<Quest>( dtoList.size() );
        for ( QuestDTO questDTO : dtoList ) {
            list.add( toEntity( questDTO ) );
        }

        return list;
    }

    @Override
    public List<QuestDTO> toDto(List<Quest> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<QuestDTO> list = new ArrayList<QuestDTO>( entityList.size() );
        for ( Quest quest : entityList ) {
            list.add( toDto( quest ) );
        }

        return list;
    }

    @Override
    public QuestDTO toDto(Quest quest) {
        if ( quest == null ) {
            return null;
        }

        QuestDTO questDTO = new QuestDTO();

        questDTO.setCompanyId( questCompanyId( quest ) );
        questDTO.setLocationAddress( questLocationAddress( quest ) );
        questDTO.setLocationId( questLocationId( quest ) );
        questDTO.setCompanyTitle( questCompanyTitle( quest ) );
        questDTO.setId( quest.getId() );
        questDTO.setSlug( quest.getSlug() );
        questDTO.setTitle( quest.getTitle() );
        questDTO.setDescription( quest.getDescription() );
        questDTO.setPlayersMinCount( quest.getPlayersMinCount() );
        questDTO.setPlayersMaxCount( quest.getPlayersMaxCount() );
        questDTO.setDurationInMinutes( quest.getDurationInMinutes() );
        questDTO.setComplexity( quest.getComplexity() );
        questDTO.setFearLevel( quest.getFearLevel() );
        questDTO.setType( quest.getType() );
        questDTO.setThematics( thematicSetToThematicDTOSet( quest.getThematics() ) );

        return questDTO;
    }

    @Override
    public Quest toEntity(QuestDTO questDTO) {
        if ( questDTO == null ) {
            return null;
        }

        Quest quest = new Quest();

        quest.setLocation( locationMapper.fromId( questDTO.getLocationId() ) );
        quest.setCompany( companyMapper.fromId( questDTO.getCompanyId() ) );
        quest.setId( questDTO.getId() );
        quest.setSlug( questDTO.getSlug() );
        quest.setTitle( questDTO.getTitle() );
        quest.setDescription( questDTO.getDescription() );
        quest.setPlayersMinCount( questDTO.getPlayersMinCount() );
        quest.setPlayersMaxCount( questDTO.getPlayersMaxCount() );
        quest.setDurationInMinutes( questDTO.getDurationInMinutes() );
        quest.setComplexity( questDTO.getComplexity() );
        quest.setFearLevel( questDTO.getFearLevel() );
        quest.setType( questDTO.getType() );
        quest.setThematics( thematicDTOSetToThematicSet( questDTO.getThematics() ) );

        return quest;
    }

    private UUID questCompanyId(Quest quest) {
        if ( quest == null ) {
            return null;
        }
        Company company = quest.getCompany();
        if ( company == null ) {
            return null;
        }
        UUID id = company.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String questLocationAddress(Quest quest) {
        if ( quest == null ) {
            return null;
        }
        Location location = quest.getLocation();
        if ( location == null ) {
            return null;
        }
        String address = location.getAddress();
        if ( address == null ) {
            return null;
        }
        return address;
    }

    private UUID questLocationId(Quest quest) {
        if ( quest == null ) {
            return null;
        }
        Location location = quest.getLocation();
        if ( location == null ) {
            return null;
        }
        UUID id = location.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String questCompanyTitle(Quest quest) {
        if ( quest == null ) {
            return null;
        }
        Company company = quest.getCompany();
        if ( company == null ) {
            return null;
        }
        String title = company.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    protected Set<ThematicDTO> thematicSetToThematicDTOSet(Set<Thematic> set) {
        if ( set == null ) {
            return null;
        }

        Set<ThematicDTO> set1 = new HashSet<ThematicDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Thematic thematic : set ) {
            set1.add( thematicMapper.toDto( thematic ) );
        }

        return set1;
    }

    protected Set<Thematic> thematicDTOSetToThematicSet(Set<ThematicDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<Thematic> set1 = new HashSet<Thematic>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( ThematicDTO thematicDTO : set ) {
            set1.add( thematicMapper.toEntity( thematicDTO ) );
        }

        return set1;
    }
}
