package com.greatescape.api.monolith.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A QuestIntegrationSetting.
 */
@Entity
@Table(name = "quest_integration_setting")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class QuestIntegrationSetting extends AbstractEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private QuestIntegrationType type;

    @Type(type = "jsonb")
    @Column(name = "settings", columnDefinition = "jsonb")
    private AbstractSettings settings;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Quest quest;

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
        // For more info see: https://bit.ly/37Zo2W3
        return getClass().hashCode();
    }

    @JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "integrationType")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = MirKvestov.class, name = "MIR_KVESTOV"),
        @JsonSubTypes.Type(value = BookForm.class, name = "BOOK_FORM"),
    })
    public abstract static class AbstractSettings implements Serializable {
        private static final long serialVersionUID = 1L;

        public abstract QuestIntegrationType getIntegrationType();
    }

    @Data
    public final static class MirKvestov extends AbstractSettings {
        private static final long serialVersionUID = 1L;

        @Override
        public QuestIntegrationType getIntegrationType() {
            return QuestIntegrationType.MIR_KVESTOV;
        }
    }

    @Data
    public final static class BookForm extends AbstractSettings {
        private static final long serialVersionUID = 1L;

        private String serviceId;

        @Override
        public QuestIntegrationType getIntegrationType() {
            return QuestIntegrationType.BOOK_FORM;
        }
    }
}
