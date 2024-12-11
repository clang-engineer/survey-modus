package com.clangengineer.exformmaker.service.mapper

import com.clangengineer.exformmaker.domain.Field
import com.clangengineer.exformmaker.domain.Form
import com.clangengineer.exformmaker.service.dto.FieldDTO
import com.clangengineer.exformmaker.service.dto.FormDTO
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface FieldMapper : EntityMapper<FieldDTO, Field> {
    @Mappings(
        Mapping(target = "form", source = "form", qualifiedByName = ["formInfo"])
    )
    override fun toDto(s: Field): FieldDTO

    @Named("formInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "title", source = "title")
    )
    fun toDtoFormInfo(form: Form): FormDTO
}
