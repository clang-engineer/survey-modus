package com.clangengineer.surveymodus.service.mapper

import com.clangengineer.surveymodus.domain.Category
import com.clangengineer.surveymodus.domain.Form
import com.clangengineer.surveymodus.domain.User
import com.clangengineer.surveymodus.service.dto.CategoryDTO
import com.clangengineer.surveymodus.service.dto.FormDTO
import com.clangengineer.surveymodus.service.dto.UserDTO
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface FormMapper : EntityMapper<FormDTO, Form> {
    @Mappings(
        Mapping(target = "user", source = "user", qualifiedByName = ["userInfo"]),
        Mapping(target = "category", source = "category", qualifiedByName = ["categoryInfo"])
    )
    override fun toDto(s: Form): FormDTO

    @Named("userInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "login", source = "login")
    )
    fun toDtoUserLogin(user: User): UserDTO

    @Named("categoryInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "title", source = "title")
    )
    fun toDtoCategory(category: Category): CategoryDTO
}
