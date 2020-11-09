package com.greatescape.api.monolith.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuestPhoto.
 */
@Getter
@Setter
@Entity
@Table(name = "quest_photo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuestPhoto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "questPhotos", allowSetters = true)
    private Quest quest;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestPhoto)) {
            return false;
        }
        return id != null && id.equals(((QuestPhoto) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "QuestPhoto{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
