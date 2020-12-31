package com.greatescape.api.monolith.repository;

import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import java.util.List;
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

    List<QuestIntegrationSetting> findAllByType(QuestIntegrationType type);
}
