package com.clangengineer.exformmaker.service.mapper

import com.clangengineer.exformmaker.domain.Company
import com.clangengineer.exformmaker.domain.CompanyForm
import com.clangengineer.exformmaker.domain.Form
import com.clangengineer.exformmaker.service.dto.CompanyDTO
import com.clangengineer.exformmaker.service.dto.CompanyFormDTO
import com.clangengineer.exformmaker.service.dto.FormDTO
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface CompanyFormMapper :
    EntityMapper<CompanyFormDTO, CompanyForm> {
    @Mappings(
        Mapping(target = "company", source = "company", qualifiedByName = ["companyInfo"]),
        Mapping(target = "form", source = "form", qualifiedByName = ["formInfo"])
    )
    override fun toDto(s: CompanyForm): CompanyFormDTO

    @Named("companyInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "title", source = "title")
    )
    fun toDtoCompany(company: Company): CompanyDTO

    @Named("formInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "title", source = "title")
    )
    fun toDtoForm(form: Form): FormDTO
}
