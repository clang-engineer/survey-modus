package com.clangengineer.exformmaker.service.mapper

import com.clangengineer.exformmaker.domain.Company
import com.clangengineer.exformmaker.domain.User
import com.clangengineer.exformmaker.service.dto.CompanyDTO
import com.clangengineer.exformmaker.service.dto.UserDTO
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface CompanyMapper : EntityMapper<CompanyDTO, Company> {
    @Mappings(
        Mapping(target = "user", source = "user", qualifiedByName = ["userLogin"])
    )
    override fun toDto(s: Company): CompanyDTO

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "login", source = "login")
    )
    fun toDtoUserLogin(user: User): UserDTO
}
