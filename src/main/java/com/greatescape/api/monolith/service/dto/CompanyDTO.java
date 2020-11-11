package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Company;
import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link Company} entity.
 */
@Getter
@Setter
public final class CompanyDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 2)
    private String slug;

    @NotNull
    @Size(min = 2)
    private String title;

    @NotNull
    @Size(min = 2)
    private String legalName;

    @Pattern(regexp = "^\\d+$")
    private String taxpayerNumber;

    @Min(value = 0)
    @Max(value = 100)
    private Integer discountInPercents;

    @Min(value = 0)
    @Max(value = 100)
    private Integer commissionInPercents;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyDTO)) {
            return false;
        }

        return id != null && id.equals(((CompanyDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CompanyDTO{" +
            "id=" + getId() +
            ", slug='" + getSlug() + "'" +
            ", title='" + getTitle() + "'" +
            ", legalName='" + getLegalName() + "'" +
            ", taxpayerNumber='" + getTaxpayerNumber() + "'" +
            ", discountInPercents=" + getDiscountInPercents() +
            ", commissionInPercents=" + getCommissionInPercents() +
            "}";
    }
}
