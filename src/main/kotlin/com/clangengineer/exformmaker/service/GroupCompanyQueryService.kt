package com.clangengineer.exformmaker.service

import com.clangengineer.exformmaker.domain.Company_
import com.clangengineer.exformmaker.domain.GroupCompany
import com.clangengineer.exformmaker.domain.GroupCompany_
import com.clangengineer.exformmaker.domain.Group_
import com.clangengineer.exformmaker.repository.GroupCompanyRepository
import com.clangengineer.exformmaker.service.criteria.GroupCompanyCriteria
import com.clangengineer.exformmaker.service.dto.GroupCompanyDTO
import com.clangengineer.exformmaker.service.mapper.GroupCompanyMapper
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
class GroupCompanyQueryService(
    private val groupCompanyRepository: GroupCompanyRepository,
    private val groupCompanyMapper: GroupCompanyMapper,
) : QueryService<GroupCompany>() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: GroupCompanyCriteria?): MutableList<GroupCompanyDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return groupCompanyMapper.toDto(groupCompanyRepository.findAll(specification))
    }

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: GroupCompanyCriteria?, page: Pageable): Page<GroupCompanyDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return groupCompanyRepository.findAll(specification, page)
            .map(groupCompanyMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun countByCriteria(criteria: GroupCompanyCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return groupCompanyRepository.count(specification)
    }

    protected fun createSpecification(criteria: GroupCompanyCriteria?): Specification<GroupCompany?> {
        var specification: Specification<GroupCompany?> = Specification.where(null)
        if (criteria != null) {
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, GroupCompany_.id))
            }
            if (criteria.groupId != null) {
                specification = specification.and(
                    buildSpecification(criteria.groupId as Filter<Long>) {
                        it.join(GroupCompany_.group, JoinType.LEFT).get(Group_.id)
                    }
                )
            }
            if (criteria.companyId != null) {
                specification = specification.and(
                    buildSpecification(criteria.companyId as Filter<Long>) {
                        it.join(GroupCompany_.company, JoinType.LEFT).get(Company_.id)
                    }
                )
            }
        }
        return specification
    }
}
