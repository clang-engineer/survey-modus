package com.clangengineer.exformmaker.service

import com.clangengineer.exformmaker.domain.Company
import com.clangengineer.exformmaker.domain.Company_
import com.clangengineer.exformmaker.domain.User_
import com.clangengineer.exformmaker.repository.CompanyRepository
import com.clangengineer.exformmaker.service.criteria.CompanyCriteria
import com.clangengineer.exformmaker.service.dto.CompanyDTO
import com.clangengineer.exformmaker.service.mapper.CompanyMapper
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
class CompanyQueryService(
    private val companyRepository: CompanyRepository,
    private val companyMapper: CompanyMapper,
) : QueryService<Company>() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: CompanyCriteria?): MutableList<CompanyDTO> {
        log.debug("find by criteria : $criteria")

        val specification = createSpecification(criteria)
        return companyMapper.toDto(companyRepository.findAll(specification))
    }

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: CompanyCriteria?, page: Pageable): Page<CompanyDTO> {
        log.debug("find by criteria : $criteria, page: $page")

        val specification = createSpecification(criteria)
        return companyRepository.findAll(specification, page)
            .map(companyMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun countByCriteria(criteria: CompanyCriteria?): Long {
        log.debug("count by criteria : $criteria")

        val specification = createSpecification(criteria)
        return companyRepository.count(specification)
    }

    protected fun createSpecification(criteria: CompanyCriteria?): Specification<Company?> {
        var specification: Specification<Company?> = Specification.where(null)
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
                specification = specification.and(buildRangeSpecification(criteria.id, Company_.id))
            }
            if (criteria.title != null) {
                specification = specification.and(buildStringSpecification(criteria.title, Company_.title))
            }
            if (criteria.description != null) {
                specification = specification.and(buildStringSpecification(criteria.description, Company_.description))
            }
            if (criteria.activated != null) {
                specification = specification.and(buildSpecification(criteria.activated, Company_.activated))
            }

            if (criteria.userId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userId as Filter<Long>) {
                        it.join(Company_.user, JoinType.LEFT).get(User_.id)
                    }
                )
            }
        }
        return specification
    }
}
