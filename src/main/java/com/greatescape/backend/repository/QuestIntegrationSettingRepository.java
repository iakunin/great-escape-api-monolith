package com.greatescape.backend.repository;

import com.greatescape.backend.domain.QuestIntegrationSetting;
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
