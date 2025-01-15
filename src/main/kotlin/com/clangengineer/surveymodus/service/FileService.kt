package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.repository.FileRepository
import com.clangengineer.surveymodus.service.dto.FileDTO
import com.clangengineer.surveymodus.service.mapper.FileMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class FileService(
    private val fileRepository: FileRepository,
    private val fileMapper: FileMapper,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun save(fileDTO: FileDTO): FileDTO {
        log.debug("Request to save File : $fileDTO")
        var file = fileMapper.toEntity(fileDTO)
        file = fileRepository.save(file)
        return fileMapper.toDto(file)
    }

    fun update(fileDTO: FileDTO): FileDTO {
        log.debug("Request to update File : {}", fileDTO)
        var file = fileMapper.toEntity(fileDTO)
        file = fileRepository.save(file)
        return fileMapper.toDto(file)
    }

    fun partialUpdate(fileDTO: FileDTO): Optional<FileDTO> {
        log.debug("Request to partially update File : {}", fileDTO)

        return fileRepository.findById(fileDTO.id)
            .map {
                fileMapper.partialUpdate(it, fileDTO)
                it
            }
            .map { fileRepository.save(it) }
            .map { fileMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<FileDTO> {
        log.debug("Request to get all Files")
        return fileRepository.findAll(pageable)
            .map(fileMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<FileDTO> {
        log.debug("Request to get File : $id")
        return fileRepository.findById(id)
            .map(fileMapper::toDto)
    }

    fun delete(id: Long) {
        log.debug("Request to delete File : $id")

        fileRepository.deleteById(id)
    }
}
