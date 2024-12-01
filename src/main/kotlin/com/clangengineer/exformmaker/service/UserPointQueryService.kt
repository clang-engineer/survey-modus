package com.clangengineer.exformmaker.service

import com.clangengineer.exformmaker.domain.* // for static metamodels
import com.clangengineer.exformmaker.domain.UserPoint
import com.clangengineer.exformmaker.repository.UserPointRepository
import com.clangengineer.exformmaker.service.criteria.UserPointCriteria
import com.clangengineer.exformmaker.service.dto.UserPointDTO
import com.clangengineer.exformmaker.service.mapper.UserPointMapper
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
 * Service for executing complex queries for [UserPoint] entities in the database.
 * The main input is a [UserPointCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [UserPointDTO] or a [Page] of [UserPointDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class UserPointQueryService(
    private val userPointRepository: UserPointRepository,
    private val userPointMapper: UserPointMapper,
) : QueryService<UserPoint>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [UserPointDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: UserPointCriteria?): MutableList<UserPointDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return userPointMapper.toDto(userPointRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [UserPointDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: UserPointCriteria?, page: Pageable): Page<UserPointDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return userPointRepository.findAll(specification, page)
            .map(userPointMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: UserPointCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return userPointRepository.count(specification)
    }

    /**
     * Function to convert [UserPointCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: UserPointCriteria?): Specification<UserPoint?> {
        var specification: Specification<UserPoint?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, UserPoint_.id))
            }
            if (criteria.userId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userId as Filter<Long>) {
                        it.join(UserPoint_.user, JoinType.LEFT).get(User_.id)
                    }
                )
            }
            if (criteria.pointId != null) {
                specification = specification.and(
                    buildSpecification(criteria.pointId as Filter<Long>) {
                        it.join(UserPoint_.point, JoinType.LEFT).get(Point_.id)
                    }
                )
            }
        }
        return specification
    }
}
