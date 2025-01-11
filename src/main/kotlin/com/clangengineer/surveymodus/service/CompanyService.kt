package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.repository.CompanyRepository
import com.clangengineer.surveymodus.service.dto.CompanyDTO
import com.clangengineer.surveymodus.service.mapper.CompanyMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class CompanyService(
    private val companyRepository: CompanyRepository,
    private val companyMapper: CompanyMapper,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun save(companyDTO: CompanyDTO): CompanyDTO {
        log.debug("Request to save Company : $companyDTO")

        var company = companyMapper.toEntity(companyDTO)
        company = companyRepository.save(company)
        return companyMapper.toDto(company)
    }

    fun update(companyDTO: CompanyDTO): CompanyDTO {
        log.debug("Request to update Company : {}", companyDTO)

        var company = companyMapper.toEntity(companyDTO)
        company = companyRepository.save(company)
        return companyMapper.toDto(company)
    }

    fun partialUpdate(companyDTO: CompanyDTO): Optional<CompanyDTO> {
        log.debug("Request to partially update Company : {}", companyDTO)

        return companyRepository.findById(companyDTO.id)
            .map {
                companyMapper.partialUpdate(it, companyDTO)
                it
            }
            .map { companyRepository.save(it) }
            .map { companyMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<CompanyDTO> {
        log.debug("Request to get all Companys")

        return companyRepository.findAll(pageable)
            .map(companyMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun findAllWithEagerRelationships(pageable: Pageable) =
        companyRepository.findAllWithToOneRelationships(pageable).map(companyMapper::toDto)

    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<CompanyDTO> {
        log.debug("Request to get Company : $id")

        return companyRepository.findOneWithToOneRelationships(id)
            .map(companyMapper::toDto)
    }

    fun delete(id: Long) {
        log.debug("Request to delete Company : $id")

        companyRepository.deleteById(id)
    }

    fun saveAll(companyTOs: List<CompanyDTO>): List<CompanyDTO> {
        log.debug("Request to save and update Companys : $companyTOs")

        return companyRepository.saveAll(companyTOs.map(companyMapper::toEntity))
            .map(companyMapper::toDto)
    }
}
