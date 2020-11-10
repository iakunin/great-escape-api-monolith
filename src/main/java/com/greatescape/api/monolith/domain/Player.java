package com.greatescape.api.monolith.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.greatescape.api.monolith.domain.enumeration.Gender;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Getter
@Setter
@Entity
@Table(name = "player")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
@AuditOverride(forClass = AbstractEntity.class)
public class Player extends AbstractEntity {

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Pattern(regexp = "^\\d+$")
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @NotNull
    @Pattern(regexp = "^\\S+@\\S+$")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "subscription_allowed")
    private Boolean subscriptionAllowed;

    /**
     * Mapping Application user (Player) to default jHipster's one
     */
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User internalUser;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JsonIgnoreProperties(value = "players", allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }
        return id != null && id.equals(((Player) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", birthday='" + getBirthday() + "'" +
            ", gender='" + getGender() + "'" +
            ", subscriptionAllowed='" + getSubscriptionAllowed() + "'" +
            "}";
    }
}
