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
import lombok.ToString;
import org.hibernate.annotations.Type;

@ToString
@Entity
@Table(name = "slot_aggregation")
@Data
public class SlotAggregation extends AbstractEntity {

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
    @Column(name = "price_original", nullable = false)
    private Integer priceOriginal;

    @Column(name = "price_with_discount", nullable = false)
    private Integer priceWithDiscount;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "discount_in_percents", nullable = false)
    private Integer discountInPercents;

    @Column(name = "discount_absolute", nullable = false)
    private Integer discountAbsolute;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "commission_in_percents", nullable = false)
    private Integer commissionInPercents;

    @Column(name = "commission_absolute", nullable = false)
    private Integer commissionAbsolute;

    @NotNull
    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Type(type = "jsonb")
    @Column(name = "external_state", columnDefinition = "jsonb")
    private Map<String, Object> externalState;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "slots", allowSetters = true)
    private Quest quest;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SlotAggregation)) {
            return false;
        }
        return id != null && id.equals(((SlotAggregation) o).id);
    }

    @Override
    public int hashCode() {
        // For more info see: https://bit.ly/37Zo2W3
        return getClass().hashCode();
    }
}
