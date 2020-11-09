package com.greatescape.api.monolith.domain;

import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class LocationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Location.class);
        Location location1 = new Location();
        location1.setId(UUID.fromString("a8f6cfe0-75a0-41a8-8cb7-0d68981b1f97"));
        Location location2 = new Location();
        location2.setId(location1.getId());
        assertThat(location1).isEqualTo(location2);
        location2.setId(UUID.fromString("d8227e00-d748-4d0b-8008-da12f3e6fbdb"));
        assertThat(location1).isNotEqualTo(location2);
        location1.setId(null);
        assertThat(location1).isNotEqualTo(location2);
    }
}
