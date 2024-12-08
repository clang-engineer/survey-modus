package com.clangengineer.exformmaker.service

import com.clangengineer.exformmaker.repository.CompanyFormRepository
import com.clangengineer.exformmaker.service.dto.CompanyFormDTO
import com.clangengineer.exformmaker.service.mapper.CompanyFormMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class CompanyFormService(
    private val companyFormRepository: CompanyFormRepository,
    private val companyFormMapper: CompanyFormMapper,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun save(companyFormDTO: CompanyFormDTO): CompanyFormDTO {
        log.debug("Request to save CompanyForm : $companyFormDTO")
        var companyForm = companyFormMapper.toEntity(companyFormDTO)
        companyForm = companyFormRepository.save(companyForm)
        return companyFormMapper.toDto(companyForm)
    }

    fun update(companyFormDTO: CompanyFormDTO): CompanyFormDTO {
        log.debug("Request to update CompanyForm : {}", companyFormDTO)
        var companyForm = companyFormMapper.toEntity(companyFormDTO)
        companyForm = companyFormRepository.save(companyForm)
        return companyFormMapper.toDto(companyForm)
    }

    fun partialUpdate(companyFormDTO: CompanyFormDTO): Optional<CompanyFormDTO> {
        log.debug("Request to partially update CompanyForm : {}", companyFormDTO)

        return companyFormRepository.findById(companyFormDTO.id)
            .map {
                companyFormMapper.partialUpdate(it, companyFormDTO)
                it
            }
            .map { companyFormRepository.save(it) }
            .map { companyFormMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<CompanyFormDTO> {
        log.debug("Request to get all companyForms")
        return companyFormRepository.findAll(pageable)
            .map(companyFormMapper::toDto)
    }

    fun findAllWithEagerRelationships(pageable: Pageable) =
        companyFormRepository.findAllWithEagerRelationships(pageable).map(companyFormMapper::toDto)

    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<CompanyFormDTO> {
        log.debug("Request to get CompanyForm : $id")
        return companyFormRepository.findOneWithEagerRelationships(id)
            .map(companyFormMapper::toDto)
    }

    fun delete(id: Long) {
        log.debug("Request to delete CompanyForm : $id")

        companyFormRepository.deleteById(id)
    }
}
