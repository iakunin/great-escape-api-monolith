package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class QuestDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestDTO.class);
        QuestDTO questDTO1 = new QuestDTO();
        questDTO1.setId(UUID.fromString("f4a62607-0da6-4cd9-9462-2bdf2ee698d5"));
        QuestDTO questDTO2 = new QuestDTO();
        assertThat(questDTO1).isNotEqualTo(questDTO2);
        questDTO2.setId(questDTO1.getId());
        assertThat(questDTO1).isEqualTo(questDTO2);
        questDTO2.setId(UUID.fromString("902376f9-0289-4373-a3b8-eda351c8a260"));
        assertThat(questDTO1).isNotEqualTo(questDTO2);
        questDTO1.setId(null);
        assertThat(questDTO1).isNotEqualTo(questDTO2);
    }
}
