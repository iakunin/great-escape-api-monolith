package com.greatescape.backend.domain;

import com.greatescape.backend.web.rest.TestUtil;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class QuestPhotoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestPhoto.class);
        QuestPhoto questPhoto1 = new QuestPhoto();
        questPhoto1.setId(1L);
        QuestPhoto questPhoto2 = new QuestPhoto();
        questPhoto2.setId(questPhoto1.getId());
        assertThat(questPhoto1).isEqualTo(questPhoto2);
        questPhoto2.setId(2L);
        assertThat(questPhoto1).isNotEqualTo(questPhoto2);
        questPhoto1.setId(null);
        assertThat(questPhoto1).isNotEqualTo(questPhoto2);
    }
}
