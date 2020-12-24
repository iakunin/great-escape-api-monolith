package com.greatescape.api.monolith.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Getter
@Setter
@Entity
@Table(name = "revinfo")
@RevisionEntity
public class CustomRevisionEntity implements Serializable {

    @Id
    @SequenceGenerator(name="pk_sequence",sequenceName="revinfo_rev_sequence", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator="pk_sequence")
    @RevisionNumber
    private Long rev;

    @RevisionTimestamp
    private Long timestamp;
}