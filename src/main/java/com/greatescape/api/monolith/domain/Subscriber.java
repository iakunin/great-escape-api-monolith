package com.greatescape.api.monolith.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Subscriber.
 */
@Entity
@Table(name = "subscriber")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Subscriber extends AbstractEntity {

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Pattern(regexp = "^\\S+@\\S+$")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subscriber)) {
            return false;
        }
        return id != null && id.equals(((Subscriber) o).id);
    }

    @Override
    public int hashCode() {
        // For more info see: https://bit.ly/37Zo2W3
        return getClass().hashCode();
    }
}
