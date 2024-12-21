package com.clangengineer.exformmaker.service.mapper

import com.clangengineer.exformmaker.domain.Company
import com.clangengineer.exformmaker.domain.Group
import com.clangengineer.exformmaker.domain.User
import com.clangengineer.exformmaker.service.dto.CompanyDTO
import com.clangengineer.exformmaker.service.dto.GroupDTO
import com.clangengineer.exformmaker.service.dto.UserDTO
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface GroupMapper : EntityMapper<GroupDTO, Group> {
    @Mappings(
        Mapping(target = "user", source = "user", qualifiedByName = ["userDetail"]),
        Mapping(target = "users", source = "users", qualifiedByName = ["userList"]),
        Mapping(target = "companies", source = "companies", qualifiedByName = ["companyList"])
    )
    override fun toDto(s: Group): GroupDTO

    @Named("userDetail")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"),
        Mapping(target = "login", source = "login"),
        Mapping(target = "firstName", source = "firstName"),
        Mapping(target = "lastName", source = "lastName"),
        Mapping(target = "email", source = "email"),
    )
    fun toDtoUserLogin(user: User): UserDTO

    @Named("userList")
    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(qualifiedByName = ["userDetail"])
    fun toDtoUsers(users: MutableSet<User>): MutableSet<UserDTO>

    @Named("companyList")
    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(qualifiedByName = ["companyDetails"])
    fun toDtoCompanies(companies: MutableSet<Company>): MutableSet<CompanyDTO>

    @Named("companyDetails")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(Mapping(target = "id", source = "id"), Mapping(target = "title", source = "title"))
    fun toDtoCompanyDetails(company: Company): CompanyDTO
}
