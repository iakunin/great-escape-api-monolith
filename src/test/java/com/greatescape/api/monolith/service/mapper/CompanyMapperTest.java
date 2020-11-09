package com.greatescape.api.monolith.service.mapper;

import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CompanyMapperTest {

    private CompanyMapper companyMapper;

    @BeforeEach
    public void setUp() {
        companyMapper = new CompanyMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        final UUID id = UUID.fromString("5c9cc85d-41e7-47d9-bf1b-b59ecd8563a2");
        Assertions.assertThat(companyMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(companyMapper.fromId(null)).isNull();
    }
}
