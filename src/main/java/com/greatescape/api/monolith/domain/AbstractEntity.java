package com.greatescape.api.monolith.domain;

import com.vladmihalcea.hibernate.type.basic.ZoneIdType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Base abstract class for all user-defined entities.
 */
@MappedSuperclass
@Getter
@Setter
@ToString
@TypeDefs({
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class),
    @TypeDef(typeClass = ZoneIdType.class, defaultForType = ZoneId.class),
})
abstract public class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    protected UUID id;

    @Column(updatable = false)
    @CreationTimestamp
    protected ZonedDateTime createdAt;

    @UpdateTimestamp
    protected ZonedDateTime updatedAt;
}
