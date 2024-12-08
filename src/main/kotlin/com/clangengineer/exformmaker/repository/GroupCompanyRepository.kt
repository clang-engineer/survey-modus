package com.clangengineer.exformmaker.repository

import com.clangengineer.exformmaker.domain.GroupCompany
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GroupCompanyRepository : JpaRepository<GroupCompany, Long>, JpaSpecificationExecutor<GroupCompany> {

    @JvmDefault fun findOneWithEagerRelationships(id: Long): Optional<GroupCompany> {
        return this.findOneWithToOneRelationships(id)
    }

    @JvmDefault fun findAllWithEagerRelationships(): MutableList<GroupCompany> {
        return this.findAllWithToOneRelationships()
    }

    @JvmDefault fun findAllWithEagerRelationships(pageable: Pageable): Page<GroupCompany> {
        return this.findAllWithToOneRelationships(pageable)
    }

    @Query(
        value = "select distinct groupCompany from GroupCompany groupCompany left join fetch groupCompany.group left join fetch groupCompany.company",
        countQuery = "select count(distinct groupCompany) from GroupCompany groupCompany"
    )
    fun findAllWithToOneRelationships(pageable: Pageable): Page<GroupCompany>

    @Query("select distinct groupCompany from GroupCompany groupCompany left join fetch groupCompany.group left join fetch groupCompany.company")
    fun findAllWithToOneRelationships(): MutableList<GroupCompany>

    @Query("select groupCompany from GroupCompany groupCompany left join fetch groupCompany.group left join fetch groupCompany.company where groupCompany.id =:id")
    fun findOneWithToOneRelationships(@Param("id") id: Long): Optional<GroupCompany>
}
