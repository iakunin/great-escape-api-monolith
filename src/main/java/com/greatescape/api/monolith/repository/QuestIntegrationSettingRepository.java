package com.greatescape.api.monolith.repository;

import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the QuestIntegrationSetting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestIntegrationSettingRepository extends JpaRepository<QuestIntegrationSetting, Long>, JpaSpecificationExecutor<QuestIntegrationSetting> {
}
