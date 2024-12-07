package com.clangengineer.exformmaker.service

import com.clangengineer.exformmaker.domain.* // for static metamodels
import com.clangengineer.exformmaker.domain.UserCompany
import com.clangengineer.exformmaker.repository.UserCompanyRepository
import com.clangengineer.exformmaker.service.criteria.UserCompanyCriteria
import com.clangengineer.exformmaker.service.dto.UserCompanyDTO
import com.clangengineer.exformmaker.service.mapper.UserCompanyMapper
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
class UserCompanyQueryService(
    private val userCompanyRepository: UserCompanyRepository,
    private val userCompanyMapper: UserCompanyMapper,
) : QueryService<UserCompany>() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: UserCompanyCriteria?): MutableList<UserCompanyDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return userCompanyMapper.toDto(userCompanyRepository.findAll(specification))
    }

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: UserCompanyCriteria?, page: Pageable): Page<UserCompanyDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return userCompanyRepository.findAll(specification, page)
            .map(userCompanyMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun countByCriteria(criteria: UserCompanyCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return userCompanyRepository.count(specification)
    }

    protected fun createSpecification(criteria: UserCompanyCriteria?): Specification<UserCompany?> {
        var specification: Specification<UserCompany?> = Specification.where(null)
        if (criteria != null) {
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, UserCompany_.id))
            }
            if (criteria.userId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userId as Filter<Long>) {
                        it.join(UserCompany_.user, JoinType.LEFT).get(User_.id)
                    }
                )
            }
            if (criteria.companyId != null) {
                specification = specification.and(
                    buildSpecification(criteria.companyId as Filter<Long>) {
                        it.join(UserCompany_.company, JoinType.LEFT).get(Company_.id)
                    }
                )
            }
        }
        return specification
    }
}
