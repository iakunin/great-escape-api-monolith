package com.greatescape.api.monolith.domain;

import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class QuestPhotoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestPhoto.class);
        QuestPhoto questPhoto1 = new QuestPhoto();
        questPhoto1.setId(UUID.fromString("2813b166-5db8-48bd-b8db-37ab22d8dff1"));
        QuestPhoto questPhoto2 = new QuestPhoto();
        questPhoto2.setId(questPhoto1.getId());
        assertThat(questPhoto1).isEqualTo(questPhoto2);
        questPhoto2.setId(UUID.fromString("96fda573-e35b-479f-9e9f-de2bc56b6529"));
        assertThat(questPhoto1).isNotEqualTo(questPhoto2);
        questPhoto1.setId(null);
        assertThat(questPhoto1).isNotEqualTo(questPhoto2);
    }
}
