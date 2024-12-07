package com.clangengineer.exformmaker.repository

import com.clangengineer.exformmaker.domain.UserCompany
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserCompanyRepository : JpaRepository<UserCompany, Long>, JpaSpecificationExecutor<UserCompany> {

    @Query("select userCompany from UserCompany userCompany where userCompany.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<UserCompany>

    @JvmDefault fun findOneWithEagerRelationships(id: Long): Optional<UserCompany> {
        return this.findOneWithToOneRelationships(id)
    }

    @JvmDefault fun findAllWithEagerRelationships(): MutableList<UserCompany> {
        return this.findAllWithToOneRelationships()
    }

    @JvmDefault fun findAllWithEagerRelationships(pageable: Pageable): Page<UserCompany> {
        return this.findAllWithToOneRelationships(pageable)
    }

    @Query(
        value = "select distinct userCompany from UserCompany userCompany left join fetch userCompany.user",
        countQuery = "select count(distinct userCompany) from UserCompany userCompany"
    )
    fun findAllWithToOneRelationships(pageable: Pageable): Page<UserCompany>

    @Query("select distinct userCompany from UserCompany userCompany left join fetch userCompany.user")
    fun findAllWithToOneRelationships(): MutableList<UserCompany>

    @Query("select userCompany from UserCompany userCompany left join fetch userCompany.user where userCompany.id =:id")
    fun findOneWithToOneRelationships(@Param("id") id: Long): Optional<UserCompany>
}
