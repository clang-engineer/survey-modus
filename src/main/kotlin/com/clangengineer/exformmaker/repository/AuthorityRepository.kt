package com.clangengineer.exformmaker.repository

import com.clangengineer.exformmaker.domain.Authority
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Spring Data JPA repository for the [Authority] entity.
 */

interface AuthorityRepository : JpaRepository<Authority, String>
