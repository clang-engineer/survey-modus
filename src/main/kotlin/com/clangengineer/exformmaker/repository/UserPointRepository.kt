package com.clangengineer.exformmaker.repository

import com.clangengineer.exformmaker.domain.UserPoint
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

/**
 * Spring Data JPA repository for the UserPoint entity.
 */
@Repository
interface UserPointRepository : JpaRepository<UserPoint, Long>, JpaSpecificationExecutor<UserPoint> {

    @Query("select userPoint from UserPoint userPoint where userPoint.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<UserPoint>

    @JvmDefault fun findOneWithEagerRelationships(id: Long): Optional<UserPoint> {
        return this.findOneWithToOneRelationships(id)
    }

    @JvmDefault fun findAllWithEagerRelationships(): MutableList<UserPoint> {
        return this.findAllWithToOneRelationships()
    }

    @JvmDefault fun findAllWithEagerRelationships(pageable: Pageable): Page<UserPoint> {
        return this.findAllWithToOneRelationships(pageable)
    }

    @Query(
        value = "select distinct userPoint from UserPoint userPoint left join fetch userPoint.user",
        countQuery = "select count(distinct userPoint) from UserPoint userPoint"
    )
    fun findAllWithToOneRelationships(pageable: Pageable): Page<UserPoint>

    @Query("select distinct userPoint from UserPoint userPoint left join fetch userPoint.user")
    fun findAllWithToOneRelationships(): MutableList<UserPoint>

    @Query("select userPoint from UserPoint userPoint left join fetch userPoint.user where userPoint.id =:id")
    fun findOneWithToOneRelationships(@Param("id") id: Long): Optional<UserPoint>
}
