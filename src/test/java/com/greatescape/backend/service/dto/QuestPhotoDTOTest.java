package com.greatescape.backend.service.dto;

import com.greatescape.backend.web.rest.TestUtil;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class QuestPhotoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestPhotoDTO.class);
        QuestPhotoDTO questPhotoDTO1 = new QuestPhotoDTO();
        questPhotoDTO1.setId(1L);
        QuestPhotoDTO questPhotoDTO2 = new QuestPhotoDTO();
        assertThat(questPhotoDTO1).isNotEqualTo(questPhotoDTO2);
        questPhotoDTO2.setId(questPhotoDTO1.getId());
        assertThat(questPhotoDTO1).isEqualTo(questPhotoDTO2);
        questPhotoDTO2.setId(2L);
        assertThat(questPhotoDTO1).isNotEqualTo(questPhotoDTO2);
        questPhotoDTO1.setId(null);
        assertThat(questPhotoDTO1).isNotEqualTo(questPhotoDTO2);
    }
}
