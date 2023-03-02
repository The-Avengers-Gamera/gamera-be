package com.avengers.gamera.mapper;

import com.avengers.gamera.dto.authority.AuthorityGetDto;
import com.avengers.gamera.entity.Authority;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorityMapper {
//    Authority authorityGetDtoToAuthority(AuthorityGetDto authorityGetDto);

    AuthorityGetDto authorityToAuthorityGetDto(Authority authority);

}
