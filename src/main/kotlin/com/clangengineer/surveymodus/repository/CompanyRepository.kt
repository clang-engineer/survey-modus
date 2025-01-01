package com.clangengineer.surveymodus.repository

import com.clangengineer.surveymodus.domain.Company
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
interface CompanyRepository : JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    @Query("select company from Company company where company.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<Company>

    @Query(
        value = "select distinct company from Company company left join fetch company.user",
        countQuery = "select count(distinct g) from Company g"
    )
    fun findAllWithToOneRelationships(pageable: Pageable): Page<Company>

    @Query("select distinct company from Company company left join fetch company.user")
    fun findAllWithToOneRelationships(): MutableList<Company>

    @Query("select company from Company company left join fetch company.user where company.id =:id")
    fun findOneWithToOneRelationships(@Param("id") id: Long?): Optional<Company>
}
