package com.greatescape.api.monolith.domain;

import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class QuestTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quest.class);
        Quest quest1 = new Quest();
        quest1.setId(UUID.fromString("a13f62a6-70f7-432d-a31a-ba3a57a0ef54"));
        Quest quest2 = new Quest();
        quest2.setId(quest1.getId());
        assertThat(quest1).isEqualTo(quest2);
        quest2.setId(UUID.fromString("ed863073-df28-4a28-ada2-929382822354"));
        assertThat(quest1).isNotEqualTo(quest2);
        quest1.setId(null);
        assertThat(quest1).isNotEqualTo(quest2);
    }
}
