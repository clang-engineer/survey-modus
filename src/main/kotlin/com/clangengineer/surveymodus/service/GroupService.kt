package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.repository.GroupRepository
import com.clangengineer.surveymodus.service.dto.GroupDTO
import com.clangengineer.surveymodus.service.mapper.GroupMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class GroupService(
    private val groupRepository: GroupRepository,
    private val groupMapper: GroupMapper,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun save(gruopDTO: GroupDTO): GroupDTO {
        log.debug("Request to save Group : $gruopDTO")

        var gruop = groupMapper.toEntity(gruopDTO)
        gruop = groupRepository.save(gruop)
        return groupMapper.toDto(gruop)
    }

    fun update(gruopDTO: GroupDTO): GroupDTO {
        log.debug("Request to update Group : {}", gruopDTO)

        var gruop = groupMapper.toEntity(gruopDTO)
        gruop = groupRepository.save(gruop)
        return groupMapper.toDto(gruop)
    }

    fun partialUpdate(gruopDTO: GroupDTO): Optional<GroupDTO> {
        log.debug("Request to partially update Group : {}", gruopDTO)

        return groupRepository.findById(gruopDTO.id)
            .map {
                groupMapper.partialUpdate(it, gruopDTO)
                it
            }
            .map { groupRepository.save(it) }
            .map { groupMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<GroupDTO> {
        log.debug("Request to get all Groups")

        return groupRepository.findAll(pageable)
            .map(groupMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun findAllWithEagerRelationships(pageable: Pageable) =
        groupRepository.findAllWithToOneRelationships(pageable).map(groupMapper::toDto)

    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<GroupDTO> {
        log.debug("Request to get Group : $id")

        return groupRepository.findOneWithToOneRelationships(id)
            .map(groupMapper::toDto)
    }

    fun delete(id: Long) {
        log.debug("Request to delete Group : $id")

        groupRepository.deleteById(id)
    }

    fun saveAll(groupDTOs: List<GroupDTO>): List<GroupDTO> {
        log.debug("Request to save and update Groups : $groupDTOs")

        return groupRepository.saveAll(groupDTOs.map(groupMapper::toEntity))
            .map(groupMapper::toDto)
    }
}
