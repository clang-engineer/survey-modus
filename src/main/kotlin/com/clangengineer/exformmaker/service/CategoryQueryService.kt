package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.domain.Category
import com.clangengineer.surveymodus.domain.Category_
import com.clangengineer.surveymodus.repository.CategoryRepository
import com.clangengineer.surveymodus.service.criteria.CategoryCriteria
import com.clangengineer.surveymodus.service.dto.CategoryDTO
import com.clangengineer.surveymodus.service.mapper.CategoryMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.jhipster.service.QueryService

@Service
@Transactional(readOnly = true)
class CategoryQueryService(
    private val categoryRepository: CategoryRepository,
    private val categoryMapper: CategoryMapper,
) : QueryService<Category>() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: CategoryCriteria?): MutableList<CategoryDTO> {
        log.debug("find by criteria : $criteria")

        val specification = createSpecification(criteria)
        return categoryMapper.toDto(categoryRepository.findAll(specification))
    }

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: CategoryCriteria?, page: Pageable): Page<CategoryDTO> {
        log.debug("find by criteria : $criteria, page: $page")

        val specification = createSpecification(criteria)
        return categoryRepository.findAll(specification, page)
            .map(categoryMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun countByCriteria(criteria: CategoryCriteria?): Long {
        log.debug("count by criteria : $criteria")

        val specification = createSpecification(criteria)
        return categoryRepository.count(specification)
    }

    protected fun createSpecification(criteria: CategoryCriteria?): Specification<Category?> {
        var specification: Specification<Category?> = Specification.where(null)
        if (criteria != null) {
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and { root, query, cb ->
                    query.distinct(distinctCriteria)
                    null
                }
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, Category_.id))
            }
            if (criteria.title != null) {
                specification = specification.and(buildStringSpecification(criteria.title, Category_.title))
            }
            if (criteria.description != null) {
                specification = specification.and(buildStringSpecification(criteria.description, Category_.description))
            }
            if (criteria.activated != null) {
                specification = specification.and(buildSpecification(criteria.activated, Category_.activated))
            }
        }
        return specification
    }
}
