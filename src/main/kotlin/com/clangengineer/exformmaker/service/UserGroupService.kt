package com.clangengineer.exformmaker.service

import com.clangengineer.exformmaker.repository.UserGroupRepository
import com.clangengineer.exformmaker.service.dto.UserGroupDTO
import com.clangengineer.exformmaker.service.mapper.UserGroupMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UserGroupService(
    private val userGroupRepository: UserGroupRepository,
    private val userGroupMapper: UserGroupMapper,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun save(userGroupDTO: UserGroupDTO): UserGroupDTO {
        log.debug("Request to save UserGroup : $userGroupDTO")
        var userGroup = userGroupMapper.toEntity(userGroupDTO)
        userGroup = userGroupRepository.save(userGroup)
        return userGroupMapper.toDto(userGroup)
    }

    fun update(userGroupDTO: UserGroupDTO): UserGroupDTO {
        log.debug("Request to update UserGroup : {}", userGroupDTO)
        var userGroup = userGroupMapper.toEntity(userGroupDTO)
        userGroup = userGroupRepository.save(userGroup)
        return userGroupMapper.toDto(userGroup)
    }

    fun partialUpdate(userGroupDTO: UserGroupDTO): Optional<UserGroupDTO> {
        log.debug("Request to partially update UserGroup : {}", userGroupDTO)

        return userGroupRepository.findById(userGroupDTO.id)
            .map {
                userGroupMapper.partialUpdate(it, userGroupDTO)
                it
            }
            .map { userGroupRepository.save(it) }
            .map { userGroupMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<UserGroupDTO> {
        log.debug("Request to get all UserGroups")
        return userGroupRepository.findAll(pageable)
            .map(userGroupMapper::toDto)
    }

    fun findAllWithEagerRelationships(pageable: Pageable) =
        userGroupRepository.findAllWithEagerRelationships(pageable).map(userGroupMapper::toDto)

    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<UserGroupDTO> {
        log.debug("Request to get UserGroup : $id")
        return userGroupRepository.findOneWithEagerRelationships(id)
            .map(userGroupMapper::toDto)
    }

    fun delete(id: Long) {
        log.debug("Request to delete UserGroup : $id")

        userGroupRepository.deleteById(id)
    }
}
