package com.clangengineer.exformmaker.repository

import com.clangengineer.exformmaker.domain.Point
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

/**
 * Spring Data JPA repository for the Point entity.
 */
@Repository
interface PointRepository : JpaRepository<Point, Long>, JpaSpecificationExecutor<Point> {

    @Query("select point from Point point where point.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<Point>

    @JvmDefault fun findOneWithEagerRelationships(id: Long): Optional<Point> {
        return this.findOneWithToOneRelationships(id)
    }

    @JvmDefault fun findAllWithEagerRelationships(): MutableList<Point> {
        return this.findAllWithToOneRelationships()
    }

    @JvmDefault fun findAllWithEagerRelationships(pageable: Pageable): Page<Point> {
        return this.findAllWithToOneRelationships(pageable)
    }

    @Query(
        value = "select distinct point from Point point left join fetch point.user",
        countQuery = "select count(distinct point) from Point point"
    )
    fun findAllWithToOneRelationships(pageable: Pageable): Page<Point>

    @Query("select distinct point from Point point left join fetch point.user")
    fun findAllWithToOneRelationships(): MutableList<Point>

    @Query("select point from Point point left join fetch point.user where point.id =:id")
    fun findOneWithToOneRelationships(@Param("id") id: Long): Optional<Point>
}
