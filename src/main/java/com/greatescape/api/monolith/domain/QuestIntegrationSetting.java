package com.greatescape.api.monolith.domain;

import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A QuestIntegrationSetting.
 */
@Getter
@Setter
@Entity
@Table(name = "quest_integration_setting")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuestIntegrationSetting extends AbstractEntity {

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

    @Override
    public String toString() {
        return "QuestIntegrationSetting{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", settings='" + getSettings() + "'" +
            "}";
    }
}
