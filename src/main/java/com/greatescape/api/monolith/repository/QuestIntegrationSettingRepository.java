package com.greatescape.api.monolith.repository;

import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the QuestIntegrationSetting entity.
 */
@Repository
public interface QuestIntegrationSettingRepository extends
    JpaRepository<QuestIntegrationSetting, UUID>,
    JpaSpecificationExecutor<QuestIntegrationSetting> {

    Optional<QuestIntegrationSetting> findOneByQuest(Quest quest);
}
