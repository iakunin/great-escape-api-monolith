package com.greatescape.backend.service.dto;

import com.greatescape.backend.web.rest.TestUtil;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class QuestDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestDTO.class);
        QuestDTO questDTO1 = new QuestDTO();
        questDTO1.setId(1L);
        QuestDTO questDTO2 = new QuestDTO();
        assertThat(questDTO1).isNotEqualTo(questDTO2);
        questDTO2.setId(questDTO1.getId());
        assertThat(questDTO1).isEqualTo(questDTO2);
        questDTO2.setId(2L);
        assertThat(questDTO1).isNotEqualTo(questDTO2);
        questDTO1.setId(null);
        assertThat(questDTO1).isNotEqualTo(questDTO2);
    }
}
