package com.greatescape.api.monolith.service.mapper;


import com.greatescape.api.monolith.domain.Company;
import com.greatescape.api.monolith.service.dto.CompanyDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {



    default Company fromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
