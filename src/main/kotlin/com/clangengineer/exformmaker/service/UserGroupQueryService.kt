package com.clangengineer.exformmaker.service

import com.clangengineer.exformmaker.domain.* // for static metamodels
import com.clangengineer.exformmaker.domain.UserGroup
import com.clangengineer.exformmaker.repository.UserGroupRepository
import com.clangengineer.exformmaker.service.criteria.UserGroupCriteria
import com.clangengineer.exformmaker.service.dto.UserGroupDTO
import com.clangengineer.exformmaker.service.mapper.UserGroupMapper
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
class UserGroupQueryService(
    private val userGroupRepository: UserGroupRepository,
    private val userGroupMapper: UserGroupMapper,
) : QueryService<UserGroup>() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: UserGroupCriteria?): MutableList<UserGroupDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return userGroupMapper.toDto(userGroupRepository.findAll(specification))
    }

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: UserGroupCriteria?, page: Pageable): Page<UserGroupDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return userGroupRepository.findAll(specification, page)
            .map(userGroupMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun countByCriteria(criteria: UserGroupCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return userGroupRepository.count(specification)
    }

    protected fun createSpecification(criteria: UserGroupCriteria?): Specification<UserGroup?> {
        var specification: Specification<UserGroup?> = Specification.where(null)
        if (criteria != null) {
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, UserGroup_.id))
            }
            if (criteria.userId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userId as Filter<Long>) {
                        it.join(UserGroup_.user, JoinType.LEFT).get(User_.id)
                    }
                )
            }
            if (criteria.groupId != null) {
                specification = specification.and(
                    buildSpecification(criteria.groupId as Filter<Long>) {
                        it.join(UserGroup_.group, JoinType.LEFT).get(Group_.id)
                    }
                )
            }
        }
        return specification
    }
}
