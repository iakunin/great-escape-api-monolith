package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class LocationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationDTO.class);
        LocationDTO locationDTO1 = new LocationDTO();
        locationDTO1.setId(UUID.fromString("186d88e8-5c95-4d9a-8ca0-e109225fd73d"));
        LocationDTO locationDTO2 = new LocationDTO();
        assertThat(locationDTO1).isNotEqualTo(locationDTO2);
        locationDTO2.setId(locationDTO1.getId());
        assertThat(locationDTO1).isEqualTo(locationDTO2);
        locationDTO2.setId(UUID.fromString("51e882c4-11a9-484f-a4d8-287a79e9ab5c"));
        assertThat(locationDTO1).isNotEqualTo(locationDTO2);
        locationDTO1.setId(null);
        assertThat(locationDTO1).isNotEqualTo(locationDTO2);
    }
}
