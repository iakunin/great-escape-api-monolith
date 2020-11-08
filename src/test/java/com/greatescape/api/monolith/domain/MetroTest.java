package com.greatescape.api.monolith.domain;

import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class MetroTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Metro.class);
        Metro metro1 = new Metro();
        metro1.setId(UUID.fromString("bcfb8eca-abbe-4880-96eb-98f4bb61798a"));
        Metro metro2 = new Metro();
        metro2.setId(metro1.getId());
        assertThat(metro1).isEqualTo(metro2);
        metro2.setId(UUID.fromString("a872f3e1-25ad-477a-9c32-b0eba66e19f3"));
        assertThat(metro1).isNotEqualTo(metro2);
        metro1.setId(null);
        assertThat(metro1).isNotEqualTo(metro2);
    }
}
