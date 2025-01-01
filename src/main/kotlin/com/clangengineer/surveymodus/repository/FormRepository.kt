package com.clangengineer.surveymodus.repository

import com.clangengineer.surveymodus.domain.Form
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
interface FormRepository : JpaRepository<Form, Long>, JpaSpecificationExecutor<Form> {

    @Query("select form from Form form where form.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<Form>

    @Query(
        value = "select distinct form from Form form left join fetch form.user left join fetch form.category",
        countQuery = "select count(distinct g) from Form g"
    )
    fun findAllWithToOneRelationships(pageable: Pageable): Page<Form>

    @Query("select distinct form from Form form left join fetch form.user left join fetch form.category")
    fun findAllWithToOneRelationships(): MutableList<Form>

    @Query("select form from Form form left join fetch form.user left join fetch form.category where form.id =:id")
    fun findOneWithToOneRelationships(@Param("id") id: Long?): Optional<Form>
}
