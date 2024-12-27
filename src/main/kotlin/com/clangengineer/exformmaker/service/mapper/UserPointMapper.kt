package com.clangengineer.surveymodus.service.mapper

import com.clangengineer.surveymodus.domain.Point
import com.clangengineer.surveymodus.domain.User
import com.clangengineer.surveymodus.domain.UserPoint
import com.clangengineer.surveymodus.service.dto.PointDTO
import com.clangengineer.surveymodus.service.dto.UserDTO
import com.clangengineer.surveymodus.service.dto.UserPointDTO
import org.mapstruct.*

/**
 * Mapper for the entity [UserPoint] and its DTO [UserPointDTO].
 */
@Mapper(componentModel = "spring")
interface UserPointMapper :
    EntityMapper<UserPointDTO, UserPoint> {

    @Mappings(
        Mapping(target = "user", source = "user", qualifiedByName = ["userInfo"]), Mapping(target = "point", source = "point", qualifiedByName = ["pointInfo"])
    )
    override fun toDto(s: UserPoint): UserPointDTO

    @Named("userInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "login", source = "login")
    )
    fun toDtoUser(user: User): UserDTO

    @Named("pointInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "title", source = "title")
    )
    fun toDtoPoint(point: Point): PointDTO
}
