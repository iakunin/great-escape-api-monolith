package com.greatescape.backend.domain;

import com.greatescape.backend.domain.enumeration.QuestIntegrationType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A QuestIntegrationSetting.
 */
@Entity
@Table(name = "quest_integration_setting")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuestIntegrationSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private QuestIntegrationType type;

    /**
     * @TODO: convert to Json-field
     */
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "settings")
    private String settings;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Quest quest;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestIntegrationType getType() {
        return type;
    }

    public QuestIntegrationSetting type(QuestIntegrationType type) {
        this.type = type;
        return this;
    }

    public void setType(QuestIntegrationType type) {
        this.type = type;
    }

    public String getSettings() {
        return settings;
    }

    public QuestIntegrationSetting settings(String settings) {
        this.settings = settings;
        return this;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public Quest getQuest() {
        return quest;
    }

    public QuestIntegrationSetting quest(Quest quest) {
        this.quest = quest;
        return this;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestIntegrationSetting)) {
            return false;
        }
        return id != null && id.equals(((QuestIntegrationSetting) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestIntegrationSetting{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", settings='" + getSettings() + "'" +
            "}";
    }
}
