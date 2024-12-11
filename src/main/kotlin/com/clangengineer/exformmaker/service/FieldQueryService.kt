package com.clangengineer.exformmaker.service

import com.clangengineer.exformmaker.domain.Field
import com.clangengineer.exformmaker.domain.Field_
import com.clangengineer.exformmaker.domain.Form_
import com.clangengineer.exformmaker.repository.FieldRepository
import com.clangengineer.exformmaker.service.criteria.FieldCriteria
import com.clangengineer.exformmaker.service.dto.FieldDTO
import com.clangengineer.exformmaker.service.mapper.FieldMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.jhipster.service.QueryService
import tech.jhipster.service.filter.Filter
import javax.persistence.criteria.JoinType

@Service
@Transactional(readOnly = true)
class FieldQueryService(
    private val fieldRepository: FieldRepository,
    private val fieldMapper: FieldMapper,
) : QueryService<Field>() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: FieldCriteria?): MutableList<FieldDTO> {
        log.debug("find by criteria : $criteria")

        val specification = createSpecification(criteria)
        return fieldMapper.toDto(fieldRepository.findAll(specification))
    }

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: FieldCriteria?, page: Pageable): Page<FieldDTO> {
        log.debug("find by criteria : $criteria, page: $page")

        val specification = createSpecification(criteria)
        return fieldRepository.findAll(specification, page)
            .map(fieldMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun countByCriteria(criteria: FieldCriteria?): Long {
        log.debug("count by criteria : $criteria")

        val specification = createSpecification(criteria)
        return fieldRepository.count(specification)
    }

    protected fun createSpecification(criteria: FieldCriteria?): Specification<Field?> {
        var specification: Specification<Field?> = Specification.where(null)
        if (criteria != null) {
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
//                specification = specification.and(distinct(distinctCriteria))
                specification = specification.and { root, query, cb ->
                    query.distinct(distinctCriteria)
                    null
                }
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, Field_.id))
            }
            if (criteria.title != null) {
                specification = specification.and(buildStringSpecification(criteria.title, Field_.title))
            }
            if (criteria.description != null) {
                specification = specification.and(buildStringSpecification(criteria.description, Field_.description))
            }
            if (criteria.activated != null) {
                specification = specification.and(buildSpecification(criteria.activated, Field_.activated))
            }

            if (criteria.formId != null) {
                specification = specification.and(
                    buildSpecification(criteria.formId as Filter<Long>) {
                        it.join(Field_.form, JoinType.LEFT).get(Form_.id)
                    }
                )
            }
        }
        return specification
    }
}
