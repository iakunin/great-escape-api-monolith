package com.greatescape.backend.service.mapper;

import com.greatescape.backend.domain.Company;
import com.greatescape.backend.domain.Player;
import com.greatescape.backend.domain.User;
import com.greatescape.backend.service.dto.PlayerDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-02T14:27:08+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.9 (Oracle Corporation)"
)
@Component
public class PlayerMapperImpl implements PlayerMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public List<Player> toEntity(List<PlayerDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Player> list = new ArrayList<Player>( dtoList.size() );
        for ( PlayerDTO playerDTO : dtoList ) {
            list.add( toEntity( playerDTO ) );
        }

        return list;
    }

    @Override
    public List<PlayerDTO> toDto(List<Player> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<PlayerDTO> list = new ArrayList<PlayerDTO>( entityList.size() );
        for ( Player player : entityList ) {
            list.add( toDto( player ) );
        }

        return list;
    }

    @Override
    public PlayerDTO toDto(Player player) {
        if ( player == null ) {
            return null;
        }

        PlayerDTO playerDTO = new PlayerDTO();

        playerDTO.setInternalUserLogin( playerInternalUserLogin( player ) );
        playerDTO.setCompanyId( playerCompanyId( player ) );
        playerDTO.setCompanyTitle( playerCompanyTitle( player ) );
        playerDTO.setInternalUserId( playerInternalUserId( player ) );
        playerDTO.setId( player.getId() );
        playerDTO.setName( player.getName() );
        playerDTO.setPhone( player.getPhone() );
        playerDTO.setEmail( player.getEmail() );
        playerDTO.setBirthday( player.getBirthday() );
        playerDTO.setGender( player.getGender() );
        playerDTO.setSubscriptionAllowed( player.isSubscriptionAllowed() );

        return playerDTO;
    }

    @Override
    public Player toEntity(PlayerDTO playerDTO) {
        if ( playerDTO == null ) {
            return null;
        }

        Player player = new Player();

        player.setInternalUser( userMapper.userFromId( playerDTO.getInternalUserId() ) );
        player.setCompany( companyMapper.fromId( playerDTO.getCompanyId() ) );
        player.setId( playerDTO.getId() );
        player.setName( playerDTO.getName() );
        player.setPhone( playerDTO.getPhone() );
        player.setEmail( playerDTO.getEmail() );
        player.setBirthday( playerDTO.getBirthday() );
        player.setGender( playerDTO.getGender() );
        player.setSubscriptionAllowed( playerDTO.isSubscriptionAllowed() );

        return player;
    }

    private String playerInternalUserLogin(Player player) {
        if ( player == null ) {
            return null;
        }
        User internalUser = player.getInternalUser();
        if ( internalUser == null ) {
            return null;
        }
        String login = internalUser.getLogin();
        if ( login == null ) {
            return null;
        }
        return login;
    }

    private Long playerCompanyId(Player player) {
        if ( player == null ) {
            return null;
        }
        Company company = player.getCompany();
        if ( company == null ) {
            return null;
        }
        Long id = company.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String playerCompanyTitle(Player player) {
        if ( player == null ) {
            return null;
        }
        Company company = player.getCompany();
        if ( company == null ) {
            return null;
        }
        String title = company.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    private Long playerInternalUserId(Player player) {
        if ( player == null ) {
            return null;
        }
        User internalUser = player.getInternalUser();
        if ( internalUser == null ) {
            return null;
        }
        Long id = internalUser.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
