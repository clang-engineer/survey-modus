package com.clangengineer.exformmaker.service.mapper

import com.clangengineer.exformmaker.domain.Group
import com.clangengineer.exformmaker.domain.User
import com.clangengineer.exformmaker.service.dto.GroupDTO
import com.clangengineer.exformmaker.service.dto.UserDTO
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface GroupMapper : EntityMapper<GroupDTO, Group> {
    @Mappings(
        Mapping(target = "user", source = "user", qualifiedByName = ["userLogin"])
    )
    override fun toDto(s: Group): GroupDTO

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "login", source = "login")
    )
    fun toDtoUserLogin(user: User): UserDTO
}
