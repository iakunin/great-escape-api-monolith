package com.greatescape.backend.service.mapper;


import com.greatescape.backend.domain.Company;
import com.greatescape.backend.service.dto.CompanyDTO;
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
