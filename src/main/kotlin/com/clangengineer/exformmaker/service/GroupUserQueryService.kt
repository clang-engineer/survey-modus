package com.clangengineer.exformmaker.service

import com.clangengineer.exformmaker.domain.GroupUser
import com.clangengineer.exformmaker.domain.GroupUser_
import com.clangengineer.exformmaker.domain.Group_
import com.clangengineer.exformmaker.domain.User_
import com.clangengineer.exformmaker.repository.GroupUserRepository
import com.clangengineer.exformmaker.service.criteria.GroupUserCriteria
import com.clangengineer.exformmaker.service.dto.GroupUserDTO
import com.clangengineer.exformmaker.service.mapper.GroupUserMapper
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
class GroupUserQueryService(
    private val groupUserRepository: GroupUserRepository,
    private val groupUserMapper: GroupUserMapper,
) : QueryService<GroupUser>() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: GroupUserCriteria?): MutableList<GroupUserDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return groupUserMapper.toDto(groupUserRepository.findAll(specification))
    }

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: GroupUserCriteria?, page: Pageable): Page<GroupUserDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return groupUserRepository.findAll(specification, page)
            .map(groupUserMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun countByCriteria(criteria: GroupUserCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return groupUserRepository.count(specification)
    }

    protected fun createSpecification(criteria: GroupUserCriteria?): Specification<GroupUser?> {
        var specification: Specification<GroupUser?> = Specification.where(null)
        if (criteria != null) {
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, GroupUser_.id))
            }
            if (criteria.userId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userId as Filter<Long>) {
                        it.join(GroupUser_.user, JoinType.LEFT).get(User_.id)
                    }
                )
            }
            if (criteria.groupId != null) {
                specification = specification.and(
                    buildSpecification(criteria.groupId as Filter<Long>) {
                        it.join(GroupUser_.group, JoinType.LEFT).get(Group_.id)
                    }
                )
            }
        }
        return specification
    }
}
