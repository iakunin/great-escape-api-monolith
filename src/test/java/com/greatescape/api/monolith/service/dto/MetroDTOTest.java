package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class MetroDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetroDTO.class);
        MetroDTO metroDTO1 = new MetroDTO();
        metroDTO1.setId(UUID.fromString("e0ab4764-ae45-4934-9422-befbb70bc851"));
        MetroDTO metroDTO2 = new MetroDTO();
        assertThat(metroDTO1).isNotEqualTo(metroDTO2);
        metroDTO2.setId(metroDTO1.getId());
        assertThat(metroDTO1).isEqualTo(metroDTO2);
        metroDTO2.setId(UUID.fromString("540f0120-d03e-4ac9-9e25-e70422f97124"));
        assertThat(metroDTO1).isNotEqualTo(metroDTO2);
        metroDTO1.setId(null);
        assertThat(metroDTO1).isNotEqualTo(metroDTO2);
    }
}
