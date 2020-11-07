package com.greatescape.backend.domain;

import com.greatescape.backend.web.rest.TestUtil;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class QuestTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quest.class);
        Quest quest1 = new Quest();
        quest1.setId(1L);
        Quest quest2 = new Quest();
        quest2.setId(quest1.getId());
        assertThat(quest1).isEqualTo(quest2);
        quest2.setId(2L);
        assertThat(quest1).isNotEqualTo(quest2);
        quest1.setId(null);
        assertThat(quest1).isNotEqualTo(quest2);
    }
}
