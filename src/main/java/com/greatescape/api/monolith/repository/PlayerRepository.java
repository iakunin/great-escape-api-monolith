package com.greatescape.api.monolith.repository;

import com.greatescape.api.monolith.domain.Player;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Player entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID>, JpaSpecificationExecutor<Player> {
    boolean existsByPhone(String phone);

    Optional<Player> findOneByEmailIgnoreCase(String email);
}
