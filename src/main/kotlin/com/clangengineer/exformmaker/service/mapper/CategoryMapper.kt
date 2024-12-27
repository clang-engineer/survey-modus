package com.clangengineer.surveymodus.service.mapper

import com.clangengineer.surveymodus.domain.Category
import com.clangengineer.surveymodus.service.dto.CategoryDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface CategoryMapper : EntityMapper<CategoryDTO, Category>
