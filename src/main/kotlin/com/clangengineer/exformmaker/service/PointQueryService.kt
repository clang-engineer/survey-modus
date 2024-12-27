package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.domain.* // for static metamodels
import com.clangengineer.surveymodus.domain.Point
import com.clangengineer.surveymodus.repository.PointRepository
import com.clangengineer.surveymodus.service.criteria.PointCriteria
import com.clangengineer.surveymodus.service.dto.PointDTO
import com.clangengineer.surveymodus.service.mapper.PointMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.jhipster.service.QueryService
import tech.jhipster.service.filter.Filter
import javax.persistence.criteria.JoinType

/**
 * Service for executing complex queries for [Point] entities in the database.
 * The main input is a [PointCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [PointDTO] or a [Page] of [PointDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class PointQueryService(
    private val pointRepository: PointRepository,
    private val pointMapper: PointMapper,
) : QueryService<Point>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [PointDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: PointCriteria?): MutableList<PointDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return pointMapper.toDto(pointRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [PointDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: PointCriteria?, page: Pageable): Page<PointDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return pointRepository.findAll(specification, page)
            .map(pointMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: PointCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return pointRepository.count(specification)
    }

    /**
     * Function to convert [PointCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: PointCriteria?): Specification<Point?> {
        var specification: Specification<Point?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, Point_.id))
            }
            if (criteria.title != null) {
                specification = specification.and(buildStringSpecification(criteria.title, Point_.title))
            }
            if (criteria.description != null) {
                specification = specification.and(buildStringSpecification(criteria.description, Point_.description))
            }
            if (criteria.activated != null) {
                specification = specification.and(buildSpecification(criteria.activated, Point_.activated))
            }
            if (criteria.type != null) {
                specification = specification.and(buildSpecification(criteria.type, Point_.type))
            }
            if (criteria.userId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userId as Filter<Long>) {
                        it.join(Point_.user, JoinType.LEFT).get(User_.id)
                    }
                )
            }
        }
        return specification
    }
}
