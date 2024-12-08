package com.clangengineer.exformmaker.service

import com.clangengineer.exformmaker.repository.GroupCompanyRepository
import com.clangengineer.exformmaker.service.dto.GroupCompanyDTO
import com.clangengineer.exformmaker.service.mapper.GroupCompanyMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class GroupCompanyService(
    private val groupCompanyRepository: GroupCompanyRepository,
    private val groupCompanyMapper: GroupCompanyMapper,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun save(groupCompanyDTO: GroupCompanyDTO): GroupCompanyDTO {
        log.debug("Request to save GroupCompany : $groupCompanyDTO")
        var groupCompany = groupCompanyMapper.toEntity(groupCompanyDTO)
        groupCompany = groupCompanyRepository.save(groupCompany)
        return groupCompanyMapper.toDto(groupCompany)
    }

    fun update(groupCompanyDTO: GroupCompanyDTO): GroupCompanyDTO {
        log.debug("Request to update GroupCompany : {}", groupCompanyDTO)
        var groupCompany = groupCompanyMapper.toEntity(groupCompanyDTO)
        groupCompany = groupCompanyRepository.save(groupCompany)
        return groupCompanyMapper.toDto(groupCompany)
    }

    fun partialUpdate(groupCompanyDTO: GroupCompanyDTO): Optional<GroupCompanyDTO> {
        log.debug("Request to partially update GroupCompany : {}", groupCompanyDTO)

        return groupCompanyRepository.findById(groupCompanyDTO.id)
            .map {
                groupCompanyMapper.partialUpdate(it, groupCompanyDTO)
                it
            }
            .map { groupCompanyRepository.save(it) }
            .map { groupCompanyMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<GroupCompanyDTO> {
        log.debug("Request to get all GroupCompanys")
        return groupCompanyRepository.findAll(pageable)
            .map(groupCompanyMapper::toDto)
    }

    fun findAllWithEagerRelationships(pageable: Pageable) =
        groupCompanyRepository.findAllWithEagerRelationships(pageable).map(groupCompanyMapper::toDto)

    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<GroupCompanyDTO> {
        log.debug("Request to get GroupCompany : $id")
        return groupCompanyRepository.findOneWithEagerRelationships(id)
            .map(groupCompanyMapper::toDto)
    }

    fun delete(id: Long) {
        log.debug("Request to delete GroupCompany : $id")

        groupCompanyRepository.deleteById(id)
    }
}
