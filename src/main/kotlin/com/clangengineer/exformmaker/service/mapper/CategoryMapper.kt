package com.clangengineer.exformmaker.service.mapper

import com.clangengineer.exformmaker.domain.Category
import com.clangengineer.exformmaker.service.dto.CategoryDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface CategoryMapper : EntityMapper<CategoryDTO, Category>
