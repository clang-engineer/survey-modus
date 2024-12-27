package com.clangengineer.surveymodus.service.mapper

import com.clangengineer.surveymodus.domain.Point
import com.clangengineer.surveymodus.domain.User
import com.clangengineer.surveymodus.service.dto.PointDTO
import com.clangengineer.surveymodus.service.dto.UserDTO
import org.mapstruct.*

/**
 * Mapper for the entity [Point] and its DTO [PointDTO].
 */
@Mapper(componentModel = "spring")
interface PointMapper :
    EntityMapper<PointDTO, Point> {

    @Mappings(
        Mapping(target = "user", source = "user", qualifiedByName = ["userLogin"])
    )
    override fun toDto(s: Point): PointDTO

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "login", source = "login")
    )
    fun toDtoUserLogin(user: User): UserDTO
}
