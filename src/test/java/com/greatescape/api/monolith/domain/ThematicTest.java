package com.greatescape.api.monolith.domain;

import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class ThematicTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Thematic.class);
        Thematic thematic1 = new Thematic();
        thematic1.setId(UUID.fromString("7a71ab5e-e18d-4b68-b404-19b2c8c2bcee"));
        Thematic thematic2 = new Thematic();
        thematic2.setId(thematic1.getId());
        assertThat(thematic1).isEqualTo(thematic2);
        thematic2.setId(UUID.fromString("431468ab-5e54-4d83-932b-05f0952056d6"));
        assertThat(thematic1).isNotEqualTo(thematic2);
        thematic1.setId(null);
        assertThat(thematic1).isNotEqualTo(thematic2);
    }
}
