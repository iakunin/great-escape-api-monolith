package com.greatescape.backend.repository;

import com.greatescape.backend.domain.QuestPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the QuestPhoto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestPhotoRepository extends JpaRepository<QuestPhoto, Long>, JpaSpecificationExecutor<QuestPhoto> {
}
