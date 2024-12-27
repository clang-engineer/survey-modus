package com.clangengineer.surveymodus.service.mapper

import com.clangengineer.surveymodus.domain.Company
import com.clangengineer.surveymodus.domain.Form
import com.clangengineer.surveymodus.domain.User
import com.clangengineer.surveymodus.domain.embeddable.Staff
import com.clangengineer.surveymodus.service.dto.CompanyDTO
import com.clangengineer.surveymodus.service.dto.FormDTO
import com.clangengineer.surveymodus.service.dto.StaffDTO
import com.clangengineer.surveymodus.service.dto.UserDTO
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface CompanyMapper : EntityMapper<CompanyDTO, Company> {
    @Mappings(
        Mapping(target = "user", source = "user", qualifiedByName = ["userLogin"]),
        Mapping(target = "forms", source = "forms", qualifiedByName = ["formList"]),
        Mapping(target = "staffs", source = "staffs", qualifiedByName = ["staffList"])
    )
    override fun toDto(s: Company): CompanyDTO

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "login", source = "login")
    )
    fun toDtoUserLogin(user: User): UserDTO

    @Named("formList")
    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(qualifiedByName = ["formDetail"]) fun toDtoForms(forms: MutableSet<Form>): MutableSet<FormDTO>

    @Named("formDetail")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "title", source = "title")
    )
    fun toDtoFormDetail(form: Form): FormDTO

    @Named("staffList")
    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(qualifiedByName = ["staffDetail"])
    fun toDtoStaffList(staffs: MutableSet<Staff>): MutableSet<StaffDTO>

    @Named("staffDetail")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "activated", source = "activated"),
        Mapping(target = "name", source = "name"),
        Mapping(target = "email", source = "email"),
        Mapping(target = "phone", source = "phone")
    )
    fun toDtoStaffDetail(staff: Staff): StaffDTO
}
