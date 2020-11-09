package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class QuestPhotoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestPhotoDTO.class);
        QuestPhotoDTO questPhotoDTO1 = new QuestPhotoDTO();
        questPhotoDTO1.setId(UUID.fromString("c0673304-8801-4e60-8ffb-8a126f10be85"));
        QuestPhotoDTO questPhotoDTO2 = new QuestPhotoDTO();
        assertThat(questPhotoDTO1).isNotEqualTo(questPhotoDTO2);
        questPhotoDTO2.setId(questPhotoDTO1.getId());
        assertThat(questPhotoDTO1).isEqualTo(questPhotoDTO2);
        questPhotoDTO2.setId(UUID.fromString("0b11749d-8a9b-47c2-b5b2-3487fc727141"));
        assertThat(questPhotoDTO1).isNotEqualTo(questPhotoDTO2);
        questPhotoDTO1.setId(null);
        assertThat(questPhotoDTO1).isNotEqualTo(questPhotoDTO2);
    }
}
