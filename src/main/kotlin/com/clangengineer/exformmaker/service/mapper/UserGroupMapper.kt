package com.clangengineer.exformmaker.service.mapper

import com.clangengineer.exformmaker.domain.Group
import com.clangengineer.exformmaker.domain.User
import com.clangengineer.exformmaker.domain.UserGroup
import com.clangengineer.exformmaker.service.dto.GroupDTO
import com.clangengineer.exformmaker.service.dto.UserDTO
import com.clangengineer.exformmaker.service.dto.UserGroupDTO
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface UserGroupMapper :
    EntityMapper<UserGroupDTO, UserGroup> {

    @Mappings(
        Mapping(target = "user", source = "user", qualifiedByName = ["userInfo"]), Mapping(target = "group", source = "group", qualifiedByName = ["groupInfo"])
    )
    override fun toDto(s: UserGroup): UserGroupDTO

    @Named("userInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "login", source = "login")
    )
    fun toDtoUser(user: User): UserDTO

    @Named("groupInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "title", source = "title")
    )
    fun toDtoGroup(group: Group): GroupDTO
}
