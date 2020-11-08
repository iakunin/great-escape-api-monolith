package com.greatescape.api.monolith.service.mapper;


import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.service.dto.PlayerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Player} and its DTO {@link PlayerDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, CompanyMapper.class})
public interface PlayerMapper extends EntityMapper<PlayerDTO, Player> {

    @Mapping(source = "internalUser.id", target = "internalUserId")
    @Mapping(source = "internalUser.login", target = "internalUserLogin")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.title", target = "companyTitle")
    PlayerDTO toDto(Player player);

    @Mapping(source = "internalUserId", target = "internalUser")
    @Mapping(source = "companyId", target = "company")
    Player toEntity(PlayerDTO playerDTO);

    default Player fromId(Long id) {
        if (id == null) {
            return null;
        }
        Player player = new Player();
        player.setId(id);
        return player;
    }
}
