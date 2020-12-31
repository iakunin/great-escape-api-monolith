package com.greatescape.api.monolith.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Table(name = "slot")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
@AuditOverride(forClass = AbstractEntity.class)
@Data
public class Slot extends AbstractEntity {

    @NotNull
    @Column(name = "date_time_local", nullable = false)
    private Instant dateTimeLocal;

    @NotNull
    @Column(name = "date_time_with_time_zone", nullable = false)
    private ZonedDateTime dateTimeWithTimeZone;

    @NotNull
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @NotNull
    @Min(value = 0)
    @Column(name = "price", nullable = false)
    private Integer price;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "discount_in_percents")
    private Integer discountInPercents;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "commission_in_percents")
    private Integer commissionInPercents;

    @Column(name = "external_id")
    private String externalId;

    /*
     * @TODO: Should be sent to integration without changes during booking creation
     */
    @Type(type = "jsonb")
    @Column(name = "external_state", columnDefinition = "jsonb")
    private Map<String, Object> externalState;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "slots", allowSetters = true)
    private Quest quest;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Slot)) {
            return false;
        }
        return id != null && id.equals(((Slot) o).id);
    }

    @Override
    public int hashCode() {
        // For more info see: https://bit.ly/37Zo2W3
        return getClass().hashCode();
    }
}
