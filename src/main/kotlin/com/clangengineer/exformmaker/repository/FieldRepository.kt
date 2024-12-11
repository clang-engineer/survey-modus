package com.clangengineer.exformmaker.repository

import com.clangengineer.exformmaker.domain.Field
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
interface FieldRepository : JpaRepository<Field, Long>, JpaSpecificationExecutor<Field> {

    @Query(
        value = "select distinct field from Field field left join fetch field.form",
        countQuery = "select count(distinct field) from Field field"
    )
    fun findAllWithToOneRelationships(pageable: Pageable): Page<Field>

    @Query("select distinct field from Field field left join fetch field.form")
    fun findAllWithToOneRelationships(): MutableList<Field>

    @Query("select field from Field field left join fetch field.form where field.id =:id")
    fun findOneWithToOneRelationships(@Param("id") id: Long?): Optional<Field>
}
