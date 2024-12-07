package com.clangengineer.exformmaker.repository

import com.clangengineer.exformmaker.domain.UserGroup
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserGroupRepository : JpaRepository<UserGroup, Long>, JpaSpecificationExecutor<UserGroup> {

    @Query("select userGroup from UserGroup userGroup where userGroup.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<UserGroup>

    @JvmDefault fun findOneWithEagerRelationships(id: Long): Optional<UserGroup> {
        return this.findOneWithToOneRelationships(id)
    }

    @JvmDefault fun findAllWithEagerRelationships(): MutableList<UserGroup> {
        return this.findAllWithToOneRelationships()
    }

    @JvmDefault fun findAllWithEagerRelationships(pageable: Pageable): Page<UserGroup> {
        return this.findAllWithToOneRelationships(pageable)
    }

    @Query(
        value = "select distinct userGroup from UserGroup userGroup left join fetch userGroup.user",
        countQuery = "select count(distinct userGroup) from UserGroup userGroup"
    )
    fun findAllWithToOneRelationships(pageable: Pageable): Page<UserGroup>

    @Query("select distinct userGroup from UserGroup userGroup left join fetch userGroup.user")
    fun findAllWithToOneRelationships(): MutableList<UserGroup>

    @Query("select userGroup from UserGroup userGroup left join fetch userGroup.user where userGroup.id =:id")
    fun findOneWithToOneRelationships(@Param("id") id: Long): Optional<UserGroup>
}
