package com.clangengineer.exformmaker.service.mapper

import com.clangengineer.exformmaker.domain.Company
import com.clangengineer.exformmaker.domain.Group
import com.clangengineer.exformmaker.domain.GroupCompany
import com.clangengineer.exformmaker.service.dto.CompanyDTO
import com.clangengineer.exformmaker.service.dto.GroupCompanyDTO
import com.clangengineer.exformmaker.service.dto.GroupDTO
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface GroupCompanyMapper :
    EntityMapper<GroupCompanyDTO, GroupCompany> {

    @Mappings(
        Mapping(target = "group", source = "group", qualifiedByName = ["groupInfo"]),
        Mapping(target = "company", source = "company", qualifiedByName = ["companyInfo"])
    )
    override fun toDto(s: GroupCompany): GroupCompanyDTO

    @Named("groupInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "title", source = "title")
    )
    fun toDtoGroup(group: Group): GroupDTO

    @Named("companyInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "title", source = "title")
    )
    fun toDtoCompany(company: Company): CompanyDTO
}
