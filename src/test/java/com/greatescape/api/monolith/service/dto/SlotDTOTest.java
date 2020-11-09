package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class SlotDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlotDTO.class);
        SlotDTO slotDTO1 = new SlotDTO();
        slotDTO1.setId(UUID.fromString("7e0a911c-5291-4c57-a3c8-3b831e9be867"));
        SlotDTO slotDTO2 = new SlotDTO();
        assertThat(slotDTO1).isNotEqualTo(slotDTO2);
        slotDTO2.setId(slotDTO1.getId());
        assertThat(slotDTO1).isEqualTo(slotDTO2);
        slotDTO2.setId(UUID.fromString("7954869e-4cc8-4551-b403-a8f4c3f710b4"));
        assertThat(slotDTO1).isNotEqualTo(slotDTO2);
        slotDTO1.setId(null);
        assertThat(slotDTO1).isNotEqualTo(slotDTO2);
    }
}
