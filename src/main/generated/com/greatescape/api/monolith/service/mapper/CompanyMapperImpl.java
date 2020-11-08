package com.greatescape.api.monolith.service.mapper;

import com.greatescape.api.monolith.domain.Company;
import com.greatescape.api.monolith.service.dto.CompanyDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-08T20:28:02+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.8 (JetBrains s.r.o.)"
)
@Component
public class CompanyMapperImpl implements CompanyMapper {

    @Override
    public Company toEntity(CompanyDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Company company = new Company();

        company.setId( dto.getId() );
        company.setSlug( dto.getSlug() );
        company.setTitle( dto.getTitle() );
        company.setLegalName( dto.getLegalName() );
        company.setTaxpayerNumber( dto.getTaxpayerNumber() );
        company.setDiscountInPercents( dto.getDiscountInPercents() );
        company.setCommissionInPercents( dto.getCommissionInPercents() );

        return company;
    }

    @Override
    public CompanyDTO toDto(Company entity) {
        if ( entity == null ) {
            return null;
        }

        CompanyDTO companyDTO = new CompanyDTO();

        companyDTO.setId( entity.getId() );
        companyDTO.setSlug( entity.getSlug() );
        companyDTO.setTitle( entity.getTitle() );
        companyDTO.setLegalName( entity.getLegalName() );
        companyDTO.setTaxpayerNumber( entity.getTaxpayerNumber() );
        companyDTO.setDiscountInPercents( entity.getDiscountInPercents() );
        companyDTO.setCommissionInPercents( entity.getCommissionInPercents() );

        return companyDTO;
    }

    @Override
    public List<Company> toEntity(List<CompanyDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Company> list = new ArrayList<Company>( dtoList.size() );
        for ( CompanyDTO companyDTO : dtoList ) {
            list.add( toEntity( companyDTO ) );
        }

        return list;
    }

    @Override
    public List<CompanyDTO> toDto(List<Company> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<CompanyDTO> list = new ArrayList<CompanyDTO>( entityList.size() );
        for ( Company company : entityList ) {
            list.add( toDto( company ) );
        }

        return list;
    }
}
