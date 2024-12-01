package com.clangengineer.exformmaker.service

import com.clangengineer.exformmaker.domain.UserPoint
import com.clangengineer.exformmaker.repository.UserPointRepository
import com.clangengineer.exformmaker.service.dto.UserPointDTO
import com.clangengineer.exformmaker.service.mapper.UserPointMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

/**
 * Service Implementation for managing [UserPoint].
 */
@Service
@Transactional
class UserPointService(
    private val userPointRepository: UserPointRepository,
    private val userPointMapper: UserPointMapper,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a userPoint.
     *
     * @param userPointDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(userPointDTO: UserPointDTO): UserPointDTO {
        log.debug("Request to save UserPoint : $userPointDTO")
        var userPoint = userPointMapper.toEntity(userPointDTO)
        userPoint = userPointRepository.save(userPoint)
        return userPointMapper.toDto(userPoint)
    }

    /**
     * Update a userPoint.
     *
     * @param userPointDTO the entity to save.
     * @return the persisted entity.
     */
    fun update(userPointDTO: UserPointDTO): UserPointDTO {
        log.debug("Request to update UserPoint : {}", userPointDTO)
        var userPoint = userPointMapper.toEntity(userPointDTO)
        userPoint = userPointRepository.save(userPoint)
        return userPointMapper.toDto(userPoint)
    }

    /**
     * Partially updates a userPoint.
     *
     * @param userPointDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(userPointDTO: UserPointDTO): Optional<UserPointDTO> {
        log.debug("Request to partially update UserPoint : {}", userPointDTO)

        return userPointRepository.findById(userPointDTO.id)
            .map {
                userPointMapper.partialUpdate(it, userPointDTO)
                it
            }
            .map { userPointRepository.save(it) }
            .map { userPointMapper.toDto(it) }
    }

    /**
     * Get all the userPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<UserPointDTO> {
        log.debug("Request to get all UserPoints")
        return userPointRepository.findAll(pageable)
            .map(userPointMapper::toDto)
    }

    /**
     * Get all the userPoints with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    fun findAllWithEagerRelationships(pageable: Pageable) =
        userPointRepository.findAllWithEagerRelationships(pageable).map(userPointMapper::toDto)

    /**
     * Get one userPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<UserPointDTO> {
        log.debug("Request to get UserPoint : $id")
        return userPointRepository.findOneWithEagerRelationships(id)
            .map(userPointMapper::toDto)
    }

    /**
     * Delete the userPoint by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long) {
        log.debug("Request to delete UserPoint : $id")

        userPointRepository.deleteById(id)
    }
}
