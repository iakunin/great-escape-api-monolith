package com.greatescape.api.monolith.repository;

import com.greatescape.api.monolith.domain.QuestPhoto;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the QuestPhoto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestPhotoRepository
    extends JpaRepository<QuestPhoto, UUID>, JpaSpecificationExecutor<QuestPhoto> {
}
