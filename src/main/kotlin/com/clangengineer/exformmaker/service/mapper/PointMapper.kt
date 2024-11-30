package com.clangengineer.exformmaker.service.mapper


import com.clangengineer.exformmaker.domain.Point
import com.clangengineer.exformmaker.service.dto.PointDTO
import org.mapstruct.Mapper

/**
 * Mapper for the entity [Point] and its DTO [PointDTO].
 */
@Mapper(componentModel = "spring")
interface PointMapper :
    EntityMapper<PointDTO, Point>
