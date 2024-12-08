package com.clangengineer.exformmaker.service

import com.clangengineer.exformmaker.repository.GroupUserRepository
import com.clangengineer.exformmaker.service.dto.GroupUserDTO
import com.clangengineer.exformmaker.service.mapper.GroupUserMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class GroupUserService(
    private val groupUserRepository: GroupUserRepository,
    private val groupUserMapper: GroupUserMapper,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun save(groupUserDTO: GroupUserDTO): GroupUserDTO {
        log.debug("Request to save GroupUser : $groupUserDTO")
        var groupUser = groupUserMapper.toEntity(groupUserDTO)
        groupUser = groupUserRepository.save(groupUser)
        return groupUserMapper.toDto(groupUser)
    }

    fun update(groupUserDTO: GroupUserDTO): GroupUserDTO {
        log.debug("Request to update GroupUser : {}", groupUserDTO)
        var groupUser = groupUserMapper.toEntity(groupUserDTO)
        groupUser = groupUserRepository.save(groupUser)
        return groupUserMapper.toDto(groupUser)
    }

    fun partialUpdate(groupUserDTO: GroupUserDTO): Optional<GroupUserDTO> {
        log.debug("Request to partially update GroupUser : {}", groupUserDTO)

        return groupUserRepository.findById(groupUserDTO.id)
            .map {
                groupUserMapper.partialUpdate(it, groupUserDTO)
                it
            }
            .map { groupUserRepository.save(it) }
            .map { groupUserMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<GroupUserDTO> {
        log.debug("Request to get all groupUsers")
        return groupUserRepository.findAll(pageable)
            .map(groupUserMapper::toDto)
    }

    fun findAllWithEagerRelationships(pageable: Pageable) =
        groupUserRepository.findAllWithEagerRelationships(pageable).map(groupUserMapper::toDto)

    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<GroupUserDTO> {
        log.debug("Request to get GroupUser : $id")
        return groupUserRepository.findOneWithEagerRelationships(id)
            .map(groupUserMapper::toDto)
    }

    fun delete(id: Long) {
        log.debug("Request to delete GroupUser : $id")

        groupUserRepository.deleteById(id)
    }
}
