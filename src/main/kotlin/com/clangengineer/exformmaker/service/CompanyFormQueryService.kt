package com.clangengineer.exformmaker.service

import com.clangengineer.exformmaker.domain.CompanyForm
import com.clangengineer.exformmaker.domain.CompanyForm_
import com.clangengineer.exformmaker.domain.Company_
import com.clangengineer.exformmaker.domain.Form_
import com.clangengineer.exformmaker.repository.CompanyFormRepository
import com.clangengineer.exformmaker.service.criteria.CompanyFormCriteria
import com.clangengineer.exformmaker.service.dto.CompanyFormDTO
import com.clangengineer.exformmaker.service.mapper.CompanyFormMapper
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
class CompanyFormQueryService(
    private val companyFormRepository: CompanyFormRepository,
    private val companyFormMapper: CompanyFormMapper,
) : QueryService<CompanyForm>() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: CompanyFormCriteria?): MutableList<CompanyFormDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return companyFormMapper.toDto(companyFormRepository.findAll(specification))
    }

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: CompanyFormCriteria?, page: Pageable): Page<CompanyFormDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return companyFormRepository.findAll(specification, page)
            .map(companyFormMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun countByCriteria(criteria: CompanyFormCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return companyFormRepository.count(specification)
    }

    protected fun createSpecification(criteria: CompanyFormCriteria?): Specification<CompanyForm?> {
        var specification: Specification<CompanyForm?> = Specification.where(null)
        if (criteria != null) {
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, CompanyForm_.id))
            }
            if (criteria.companyId != null) {
                specification = specification.and(
                    buildSpecification(criteria.companyId as Filter<Long>) {
                        it.join(CompanyForm_.company, JoinType.LEFT).get(Company_.id)
                    }
                )
            }
            if (criteria.formId != null) {
                specification = specification.and(
                    buildSpecification(criteria.formId as Filter<Long>) {
                        it.join(CompanyForm_.form, JoinType.LEFT).get(Form_.id)
                    }
                )
            }
        }
        return specification
    }
}
