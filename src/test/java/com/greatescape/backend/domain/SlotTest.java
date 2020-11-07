package com.greatescape.backend.domain;

import com.greatescape.backend.web.rest.TestUtil;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class SlotTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Slot.class);
        Slot slot1 = new Slot();
        slot1.setId(1L);
        Slot slot2 = new Slot();
        slot2.setId(slot1.getId());
        assertThat(slot1).isEqualTo(slot2);
        slot2.setId(2L);
        assertThat(slot1).isNotEqualTo(slot2);
        slot1.setId(null);
        assertThat(slot1).isNotEqualTo(slot2);
    }
}
