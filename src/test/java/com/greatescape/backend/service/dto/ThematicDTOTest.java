package com.greatescape.backend.service.dto;

import com.greatescape.backend.web.rest.TestUtil;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class ThematicDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThematicDTO.class);
        ThematicDTO thematicDTO1 = new ThematicDTO();
        thematicDTO1.setId(1L);
        ThematicDTO thematicDTO2 = new ThematicDTO();
        assertThat(thematicDTO1).isNotEqualTo(thematicDTO2);
        thematicDTO2.setId(thematicDTO1.getId());
        assertThat(thematicDTO1).isEqualTo(thematicDTO2);
        thematicDTO2.setId(2L);
        assertThat(thematicDTO1).isNotEqualTo(thematicDTO2);
        thematicDTO1.setId(null);
        assertThat(thematicDTO1).isNotEqualTo(thematicDTO2);
    }
}
