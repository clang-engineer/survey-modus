package com.clangengineer.exformmaker.service.mapper

import com.clangengineer.exformmaker.domain.Group
import com.clangengineer.exformmaker.domain.GroupUser
import com.clangengineer.exformmaker.domain.User
import com.clangengineer.exformmaker.service.dto.GroupDTO
import com.clangengineer.exformmaker.service.dto.GroupUserDTO
import com.clangengineer.exformmaker.service.dto.UserDTO
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface GroupUserMapper :
    EntityMapper<GroupUserDTO, GroupUser> {
    @Mappings(
        Mapping(target = "group", source = "group", qualifiedByName = ["groupInfo"]),
        Mapping(target = "user", source = "user", qualifiedByName = ["userInfo"])
    )
    override fun toDto(s: GroupUser): GroupUserDTO

    @Named("groupInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "title", source = "title")
    )
    fun toDtoGroup(group: Group): GroupDTO

    @Named("userInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "login", source = "login")
    )
    fun toDtoUser(user: User): UserDTO
}
