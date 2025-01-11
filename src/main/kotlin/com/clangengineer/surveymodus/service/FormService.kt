package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.repository.FormRepository
import com.clangengineer.surveymodus.service.dto.FormDTO
import com.clangengineer.surveymodus.service.mapper.FormMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class FormService(
    private val formRepository: FormRepository,
    private val formMapper: FormMapper,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun save(gruopDTO: FormDTO): FormDTO {
        log.debug("Request to save Form : $gruopDTO")

        var gruop = formMapper.toEntity(gruopDTO)
        gruop = formRepository.save(gruop)
        return formMapper.toDto(gruop)
    }

    fun update(gruopDTO: FormDTO): FormDTO {
        log.debug("Request to update Form : {}", gruopDTO)

        var gruop = formMapper.toEntity(gruopDTO)
        gruop = formRepository.save(gruop)
        return formMapper.toDto(gruop)
    }

    fun partialUpdate(gruopDTO: FormDTO): Optional<FormDTO> {
        log.debug("Request to partially update Form : {}", gruopDTO)

        return formRepository.findById(gruopDTO.id)
            .map {
                formMapper.partialUpdate(it, gruopDTO)
                it
            }
            .map { formRepository.save(it) }
            .map { formMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<FormDTO> {
        log.debug("Request to get all Forms")

        return formRepository.findAll(pageable)
            .map(formMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun findAllWithEagerRelationships(pageable: Pageable) =
        formRepository.findAllWithToOneRelationships(pageable).map(formMapper::toDto)

    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<FormDTO> {
        log.debug("Request to get Form : $id")

        return formRepository.findOneWithToOneRelationships(id)
            .map(formMapper::toDto)
    }

    fun delete(id: Long) {
        log.debug("Request to delete Form : $id")

        formRepository.deleteById(id)
    }

    fun saveAll(formDTOs: List<FormDTO>): List<FormDTO> {
        log.debug("Request to save and update Fields : $formDTOs")

        return formRepository.saveAll(formDTOs.map(formMapper::toEntity))
            .map(formMapper::toDto)
    }
}
