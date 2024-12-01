package com.clangengineer.exformmaker.service.mapper

import com.clangengineer.exformmaker.domain.Point
import com.clangengineer.exformmaker.domain.User
import com.clangengineer.exformmaker.domain.UserPoint
import com.clangengineer.exformmaker.service.dto.PointDTO
import com.clangengineer.exformmaker.service.dto.UserDTO
import com.clangengineer.exformmaker.service.dto.UserPointDTO
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
