package com.clangengineer.exformmaker.repository

import com.clangengineer.exformmaker.domain.CompanyForm
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CompanyFormRepository : JpaRepository<CompanyForm, Long>, JpaSpecificationExecutor<CompanyForm> {

    @JvmDefault fun findOneWithEagerRelationships(id: Long): Optional<CompanyForm> {
        return this.findOneWithToOneRelationships(id)
    }

    @JvmDefault fun findAllWithEagerRelationships(): MutableList<CompanyForm> {
        return this.findAllWithToOneRelationships()
    }

    @JvmDefault fun findAllWithEagerRelationships(pageable: Pageable): Page<CompanyForm> {
        return this.findAllWithToOneRelationships(pageable)
    }

    @Query(
        value = "select distinct companyForm from CompanyForm companyForm left join fetch companyForm.company left join fetch companyForm.form",
        countQuery = "select count(distinct companyForm) from CompanyForm companyForm"
    )
    fun findAllWithToOneRelationships(pageable: Pageable): Page<CompanyForm>

    @Query("select distinct companyForm from CompanyForm companyForm left join fetch companyForm.company left join fetch companyForm.form")
    fun findAllWithToOneRelationships(): MutableList<CompanyForm>

    @Query("select companyForm from CompanyForm companyForm left join fetch companyForm.company left join fetch companyForm.form where companyForm.id =:id")
    fun findOneWithToOneRelationships(@Param("id") id: Long): Optional<CompanyForm>
}
