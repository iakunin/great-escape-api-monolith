package com.greatescape.api.monolith.domain;

import com.greatescape.api.monolith.web.rest.TestUtil;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class ThematicTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Thematic.class);
        Thematic thematic1 = new Thematic();
        thematic1.setId(1L);
        Thematic thematic2 = new Thematic();
        thematic2.setId(thematic1.getId());
        assertThat(thematic1).isEqualTo(thematic2);
        thematic2.setId(2L);
        assertThat(thematic1).isNotEqualTo(thematic2);
        thematic1.setId(null);
        assertThat(thematic1).isNotEqualTo(thematic2);
    }
}
