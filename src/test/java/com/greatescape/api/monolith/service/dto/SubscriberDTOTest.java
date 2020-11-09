package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class SubscriberDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriberDTO.class);
        SubscriberDTO subscriberDTO1 = new SubscriberDTO();
        subscriberDTO1.setId(UUID.fromString("6970a525-88fe-47e9-8123-ca03204a1b04"));
        SubscriberDTO subscriberDTO2 = new SubscriberDTO();
        assertThat(subscriberDTO1).isNotEqualTo(subscriberDTO2);
        subscriberDTO2.setId(subscriberDTO1.getId());
        assertThat(subscriberDTO1).isEqualTo(subscriberDTO2);
        subscriberDTO2.setId(UUID.fromString("03cfde5f-66f7-436e-8419-64d82ae4302d"));
        assertThat(subscriberDTO1).isNotEqualTo(subscriberDTO2);
        subscriberDTO1.setId(null);
        assertThat(subscriberDTO1).isNotEqualTo(subscriberDTO2);
    }
}
