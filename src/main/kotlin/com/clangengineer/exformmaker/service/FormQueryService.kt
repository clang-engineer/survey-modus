package com.clangengineer.exformmaker.service

import com.clangengineer.exformmaker.domain.Category_
import com.clangengineer.exformmaker.domain.Form
import com.clangengineer.exformmaker.domain.Form_
import com.clangengineer.exformmaker.domain.User_
import com.clangengineer.exformmaker.repository.FormRepository
import com.clangengineer.exformmaker.service.criteria.FormCriteria
import com.clangengineer.exformmaker.service.dto.FormDTO
import com.clangengineer.exformmaker.service.mapper.FormMapper
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
class FormQueryService(
    private val formRepository: FormRepository,
    private val formMapper: FormMapper,
) : QueryService<Form>() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: FormCriteria?): MutableList<FormDTO> {
        log.debug("find by criteria : $criteria")

        val specification = createSpecification(criteria)
        return formMapper.toDto(formRepository.findAll(specification))
    }

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: FormCriteria?, page: Pageable): Page<FormDTO> {
        log.debug("find by criteria : $criteria, page: $page")

        val specification = createSpecification(criteria)
        return formRepository.findAll(specification, page)
            .map(formMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun countByCriteria(criteria: FormCriteria?): Long {
        log.debug("count by criteria : $criteria")

        val specification = createSpecification(criteria)
        return formRepository.count(specification)
    }

    protected fun createSpecification(criteria: FormCriteria?): Specification<Form?> {
        var specification: Specification<Form?> = Specification.where(null)
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
                specification = specification.and(buildRangeSpecification(criteria.id, Form_.id))
            }
            if (criteria.title != null) {
                specification = specification.and(buildStringSpecification(criteria.title, Form_.title))
            }
            if (criteria.description != null) {
                specification = specification.and(buildStringSpecification(criteria.description, Form_.description))
            }
            if (criteria.activated != null) {
                specification = specification.and(buildSpecification(criteria.activated, Form_.activated))
            }

            if (criteria.userId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userId as Filter<Long>) {
                        it.join(Form_.user, JoinType.LEFT).get(User_.id)
                    }
                )
            }

            if (criteria.categoryId != null) {
                specification = specification.and(
                    buildSpecification(criteria.categoryId as Filter<Long>) {
                        it.join(Form_.category, JoinType.LEFT).get(Category_.id)
                    }
                )
            }
        }
        return specification
    }
}
