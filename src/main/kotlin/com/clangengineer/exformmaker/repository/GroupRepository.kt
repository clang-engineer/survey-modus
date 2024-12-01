package com.clangengineer.exformmaker.repository

import com.clangengineer.exformmaker.domain.Group
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Suppress("unused")
@Repository
interface GroupRepository : JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {

    @Query("select group from Group group where group.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<Group>

    @Query(
        value = "select distinct g from Group g left join fetch g.user",
        countQuery = "select count(distinct g) from Group g"
    )
    fun findAllWithToOneRelationships(pageable: Pageable): Page<Group>

    @Query("select distinct group from Group g left join fetch g.user")
    fun findAllWithToOneRelationships(): MutableList<Group>

    @Query("select g from Group g left join fetch g.user where g.id =:id")
    fun findOneWithToOneRelationships(@Param("id") id: Long?): Optional<Group>
}
