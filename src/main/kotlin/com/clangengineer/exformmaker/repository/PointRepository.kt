package com.clangengineer.exformmaker.repository

import com.clangengineer.exformmaker.domain.Point
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the Point entity.
 */
@Suppress("unused")
@Repository
interface PointRepository : JpaRepository<Point, Long>, JpaSpecificationExecutor<Point>
