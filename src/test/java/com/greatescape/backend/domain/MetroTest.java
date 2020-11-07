package com.greatescape.backend.domain;

import com.greatescape.backend.web.rest.TestUtil;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class MetroTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Metro.class);
        Metro metro1 = new Metro();
        metro1.setId(1L);
        Metro metro2 = new Metro();
        metro2.setId(metro1.getId());
        assertThat(metro1).isEqualTo(metro2);
        metro2.setId(2L);
        assertThat(metro1).isNotEqualTo(metro2);
        metro1.setId(null);
        assertThat(metro1).isNotEqualTo(metro2);
    }
}
