package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.domain.Point
import com.clangengineer.surveymodus.repository.PointRepository
import com.clangengineer.surveymodus.service.dto.PointDTO
import com.clangengineer.surveymodus.service.mapper.PointMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

/**
 * Service Implementation for managing [Point].
 */
@Service
@Transactional
class PointService(
    private val pointRepository: PointRepository,
    private val pointMapper: PointMapper,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a point.
     *
     * @param pointDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(pointDTO: PointDTO): PointDTO {
        log.debug("Request to save Point : $pointDTO")
        var point = pointMapper.toEntity(pointDTO)
        point = pointRepository.save(point)
        return pointMapper.toDto(point)
    }

    /**
     * Update a point.
     *
     * @param pointDTO the entity to save.
     * @return the persisted entity.
     */
    fun update(pointDTO: PointDTO): PointDTO {
        log.debug("Request to update Point : {}", pointDTO)
        var point = pointMapper.toEntity(pointDTO)
        point = pointRepository.save(point)
        return pointMapper.toDto(point)
    }

    /**
     * Partially updates a point.
     *
     * @param pointDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(pointDTO: PointDTO): Optional<PointDTO> {
        log.debug("Request to partially update Point : {}", pointDTO)

        return pointRepository.findById(pointDTO.id)
            .map {
                pointMapper.partialUpdate(it, pointDTO)
                it
            }
            .map { pointRepository.save(it) }
            .map { pointMapper.toDto(it) }
    }

    /**
     * Get all the points.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<PointDTO> {
        log.debug("Request to get all Points")
        return pointRepository.findAll(pageable)
            .map(pointMapper::toDto)
    }

    /**
     * Get all the points with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    fun findAllWithEagerRelationships(pageable: Pageable) =
        pointRepository.findAllWithEagerRelationships(pageable).map(pointMapper::toDto)

    /**
     * Get one point by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<PointDTO> {
        log.debug("Request to get Point : $id")
        return pointRepository.findOneWithEagerRelationships(id)
            .map(pointMapper::toDto)
    }

    /**
     * Delete the point by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long) {
        log.debug("Request to delete Point : $id")

        pointRepository.deleteById(id)
    }
}
