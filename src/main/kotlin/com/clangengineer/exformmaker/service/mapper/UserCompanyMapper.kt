package com.clangengineer.exformmaker.service.mapper

import com.clangengineer.exformmaker.domain.Company
import com.clangengineer.exformmaker.domain.User
import com.clangengineer.exformmaker.domain.UserCompany
import com.clangengineer.exformmaker.service.dto.CompanyDTO
import com.clangengineer.exformmaker.service.dto.UserCompanyDTO
import com.clangengineer.exformmaker.service.dto.UserDTO
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface UserCompanyMapper :
    EntityMapper<UserCompanyDTO, UserCompany> {

    @Mappings(
        Mapping(target = "user", source = "user", qualifiedByName = ["userInfo"]), Mapping(target = "company", source = "company", qualifiedByName = ["companyInfo"])
    )
    override fun toDto(s: UserCompany): UserCompanyDTO

    @Named("userInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "login", source = "login")
    )
    fun toDtoUser(user: User): UserDTO

    @Named("companyInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "title", source = "title")
    )
    fun toDtoCompany(company: Company): CompanyDTO
}
