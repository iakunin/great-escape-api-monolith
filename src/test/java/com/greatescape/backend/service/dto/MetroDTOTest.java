package com.greatescape.backend.service.dto;

import com.greatescape.backend.web.rest.TestUtil;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class MetroDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetroDTO.class);
        MetroDTO metroDTO1 = new MetroDTO();
        metroDTO1.setId(1L);
        MetroDTO metroDTO2 = new MetroDTO();
        assertThat(metroDTO1).isNotEqualTo(metroDTO2);
        metroDTO2.setId(metroDTO1.getId());
        assertThat(metroDTO1).isEqualTo(metroDTO2);
        metroDTO2.setId(2L);
        assertThat(metroDTO1).isNotEqualTo(metroDTO2);
        metroDTO1.setId(null);
        assertThat(metroDTO1).isNotEqualTo(metroDTO2);
    }
}
