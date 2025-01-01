package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.domain.Group
import com.clangengineer.surveymodus.domain.Group_
import com.clangengineer.surveymodus.domain.User_
import com.clangengineer.surveymodus.repository.GroupRepository
import com.clangengineer.surveymodus.service.criteria.GroupCriteria
import com.clangengineer.surveymodus.service.dto.GroupDTO
import com.clangengineer.surveymodus.service.mapper.GroupMapper
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
class GroupQueryService(
    private val groupRepository: GroupRepository,
    private val groupMapper: GroupMapper,
) : QueryService<Group>() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: GroupCriteria?): MutableList<GroupDTO> {
        log.debug("find by criteria : $criteria")

        val specification = createSpecification(criteria)
        return groupMapper.toDto(groupRepository.findAll(specification))
    }

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: GroupCriteria?, page: Pageable): Page<GroupDTO> {
        log.debug("find by criteria : $criteria, page: $page")

        val specification = createSpecification(criteria)
        return groupRepository.findAll(specification, page)
            .map(groupMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun countByCriteria(criteria: GroupCriteria?): Long {
        log.debug("count by criteria : $criteria")

        val specification = createSpecification(criteria)
        return groupRepository.count(specification)
    }

    protected fun createSpecification(criteria: GroupCriteria?): Specification<Group?> {
        var specification: Specification<Group?> = Specification.where(null)
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
                specification = specification.and(buildRangeSpecification(criteria.id, Group_.id))
            }
            if (criteria.title != null) {
                specification = specification.and(buildStringSpecification(criteria.title, Group_.title))
            }
            if (criteria.description != null) {
                specification = specification.and(buildStringSpecification(criteria.description, Group_.description))
            }
            if (criteria.activated != null) {
                specification = specification.and(buildSpecification(criteria.activated, Group_.activated))
            }

            if (criteria.userId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userId as Filter<Long>) {
                        it.join(Group_.user, JoinType.LEFT).get(User_.id)
                    }
                )
            }
        }
        return specification
    }
}
