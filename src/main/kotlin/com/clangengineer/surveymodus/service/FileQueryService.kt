package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.domain.File
import com.clangengineer.surveymodus.domain.File_
import com.clangengineer.surveymodus.repository.FileRepository
import com.clangengineer.surveymodus.service.criteria.FileCriteria
import com.clangengineer.surveymodus.service.dto.FileDTO
import com.clangengineer.surveymodus.service.mapper.FileMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.jhipster.service.QueryService

@Service
@Transactional(readOnly = true)
class FileQueryService(
    private val fileRepository: FileRepository,
    private val fileMapper: FileMapper,
) : QueryService<File>() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: FileCriteria?): MutableList<FileDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return fileMapper.toDto(fileRepository.findAll(specification))
    }

    @Transactional(readOnly = true)
    fun findByCriteria(criteria: FileCriteria?, page: Pageable): Page<FileDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return fileRepository.findAll(specification, page)
            .map(fileMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun countByCriteria(criteria: FileCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return fileRepository.count(specification)
    }

    protected fun createSpecification(criteria: FileCriteria?): Specification<File?> {
        var specification: Specification<File?> = Specification.where(null)
        if (criteria != null) {
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, File_.id))
            }

            if (criteria.filename != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.filename, File_.filename))
            }

            if (criteria.filepath != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.filepath, File_.filepath)
                )
            }

            if (criteria.hashKey != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.hashKey, File_.hashKey)
                )
            }
        }
        return specification
    }
}
