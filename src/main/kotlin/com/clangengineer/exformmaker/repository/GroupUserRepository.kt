package com.clangengineer.exformmaker.repository

import com.clangengineer.exformmaker.domain.GroupUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GroupUserRepository : JpaRepository<GroupUser, Long>, JpaSpecificationExecutor<GroupUser> {

    @Query("select groupUser from GroupUser groupUser where groupUser.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<GroupUser>

    @JvmDefault fun findOneWithEagerRelationships(id: Long): Optional<GroupUser> {
        return this.findOneWithToOneRelationships(id)
    }

    @JvmDefault fun findAllWithEagerRelationships(): MutableList<GroupUser> {
        return this.findAllWithToOneRelationships()
    }

    @JvmDefault fun findAllWithEagerRelationships(pageable: Pageable): Page<GroupUser> {
        return this.findAllWithToOneRelationships(pageable)
    }

    @Query(
        value = "select distinct groupUser from GroupUser groupUser left join fetch groupUser.user",
        countQuery = "select count(distinct groupUser) from GroupUser groupUser"
    )
    fun findAllWithToOneRelationships(pageable: Pageable): Page<GroupUser>

    @Query("select distinct groupUser from GroupUser groupUser left join fetch groupUser.user")
    fun findAllWithToOneRelationships(): MutableList<GroupUser>

    @Query("select groupUser from GroupUser groupUser left join fetch groupUser.user where groupUser.id =:id")
    fun findOneWithToOneRelationships(@Param("id") id: Long): Optional<GroupUser>
}
