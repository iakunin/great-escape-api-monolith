package com.greatescape.backend.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.greatescape.backend.domain.Location} entity.
 */
public class LocationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2)
    private String address;

    @Lob
    private String addressExplanation;


    private Long cityId;

    private String cityTitle;
    private Set<MetroDTO> metros = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressExplanation() {
        return addressExplanation;
    }

    public void setAddressExplanation(String addressExplanation) {
        this.addressExplanation = addressExplanation;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityTitle() {
        return cityTitle;
    }

    public void setCityTitle(String cityTitle) {
        this.cityTitle = cityTitle;
    }

    public Set<MetroDTO> getMetros() {
        return metros;
    }

    public void setMetros(Set<MetroDTO> metros) {
        this.metros = metros;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationDTO)) {
            return false;
        }

        return id != null && id.equals(((LocationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", addressExplanation='" + getAddressExplanation() + "'" +
            ", cityId=" + getCityId() +
            ", cityTitle='" + getCityTitle() + "'" +
            ", metros='" + getMetros() + "'" +
            "}";
    }
}
