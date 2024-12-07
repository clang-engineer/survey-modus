package com.clangengineer.exformmaker.service

import com.clangengineer.exformmaker.repository.UserCompanyRepository
import com.clangengineer.exformmaker.service.dto.UserCompanyDTO
import com.clangengineer.exformmaker.service.mapper.UserCompanyMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UserCompanyService(
    private val userCompanyRepository: UserCompanyRepository,
    private val userCompanyMapper: UserCompanyMapper,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun save(userCompanyDTO: UserCompanyDTO): UserCompanyDTO {
        log.debug("Request to save UserCompany : $userCompanyDTO")
        var userCompany = userCompanyMapper.toEntity(userCompanyDTO)
        userCompany = userCompanyRepository.save(userCompany)
        return userCompanyMapper.toDto(userCompany)
    }

    fun update(userCompanyDTO: UserCompanyDTO): UserCompanyDTO {
        log.debug("Request to update UserCompany : {}", userCompanyDTO)
        var userCompany = userCompanyMapper.toEntity(userCompanyDTO)
        userCompany = userCompanyRepository.save(userCompany)
        return userCompanyMapper.toDto(userCompany)
    }

    fun partialUpdate(userCompanyDTO: UserCompanyDTO): Optional<UserCompanyDTO> {
        log.debug("Request to partially update UserCompany : {}", userCompanyDTO)

        return userCompanyRepository.findById(userCompanyDTO.id)
            .map {
                userCompanyMapper.partialUpdate(it, userCompanyDTO)
                it
            }
            .map { userCompanyRepository.save(it) }
            .map { userCompanyMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<UserCompanyDTO> {
        log.debug("Request to get all UserCompanys")
        return userCompanyRepository.findAll(pageable)
            .map(userCompanyMapper::toDto)
    }

    fun findAllWithEagerRelationships(pageable: Pageable) =
        userCompanyRepository.findAllWithEagerRelationships(pageable).map(userCompanyMapper::toDto)

    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<UserCompanyDTO> {
        log.debug("Request to get UserCompany : $id")
        return userCompanyRepository.findOneWithEagerRelationships(id)
            .map(userCompanyMapper::toDto)
    }

    fun delete(id: Long) {
        log.debug("Request to delete UserCompany : $id")

        userCompanyRepository.deleteById(id)
    }
}
