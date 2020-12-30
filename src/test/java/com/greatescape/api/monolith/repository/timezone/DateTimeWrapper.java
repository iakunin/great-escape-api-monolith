package com.greatescape.api.monolith.repository.timezone;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jhi_date_time_wrapper")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DateTimeWrapper {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "instant")
    private Instant instant;

    @Column(name = "local_date_time")
    private LocalDateTime localDateTime;

    @Column(name = "offset_date_time")
    private OffsetDateTime offsetDateTime;

    @Column(name = "zoned_date_time")
    private ZonedDateTime zonedDateTime;

    @Column(name = "local_time")
    private LocalTime localTime;

    @Column(name = "offset_time")
    private OffsetTime offsetTime;

    @Column(name = "local_date")
    private LocalDate localDate;
}
