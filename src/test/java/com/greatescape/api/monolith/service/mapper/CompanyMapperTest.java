package com.greatescape.api.monolith.service.mapper;

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
        Long id = 1L;
        Assertions.assertThat(companyMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(companyMapper.fromId(null)).isNull();
    }
}
