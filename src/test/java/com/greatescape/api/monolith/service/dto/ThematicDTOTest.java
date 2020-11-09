package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class ThematicDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThematicDTO.class);
        ThematicDTO thematicDTO1 = new ThematicDTO();
        thematicDTO1.setId(UUID.fromString("9798f131-f3db-4043-9526-3a62fb51b4a1"));
        ThematicDTO thematicDTO2 = new ThematicDTO();
        assertThat(thematicDTO1).isNotEqualTo(thematicDTO2);
        thematicDTO2.setId(thematicDTO1.getId());
        assertThat(thematicDTO1).isEqualTo(thematicDTO2);
        thematicDTO2.setId(UUID.fromString("452aa922-3fd2-491f-adfe-553bbbb0349a"));
        assertThat(thematicDTO1).isNotEqualTo(thematicDTO2);
        thematicDTO1.setId(null);
        assertThat(thematicDTO1).isNotEqualTo(thematicDTO2);
    }
}
