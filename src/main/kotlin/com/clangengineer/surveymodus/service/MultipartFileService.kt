package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.config.ApplicationProperties
import com.clangengineer.surveymodus.repository.FileRepository
import com.clangengineer.surveymodus.service.dto.FileDTO
import com.clangengineer.surveymodus.service.mapper.FileMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption.REPLACE_EXISTING

@Service
@Transactional
class MultipartFileService(
    applicationProperties: ApplicationProperties,
    private val fileMapper: FileMapper,
    private val fileRepository: FileRepository
) {
    private val logger = LoggerFactory.getLogger(MultipartFileService::class.java)

    private val storage = applicationProperties.storage

    fun createEntityAndSaveMultipartFiles(multipartFiles: List<MultipartFile>): List<FileDTO> {
        logger.info("Saving multipart files")

        val uploadPath = Paths.get(storage.path)
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }

        val fileDTOList = mutableListOf<FileDTO>()
        multipartFiles.forEach { multipartFile ->
            val fileDTO = getFileDTO(multipartFile)
            val file = fileRepository.save(fileMapper.toEntity(fileDTO))

            val targetPath = uploadPath.resolve(file.hashKey)

            multipartFile.inputStream.use { inputStream ->
                Files.copy(inputStream, targetPath, REPLACE_EXISTING)
            }

            fileDTOList.add(fileMapper.toDto(file))
        }

        return fileDTOList
    }

    private fun getFileDTO(multipartFile: MultipartFile): FileDTO {
        return FileDTO(
            name = multipartFile.originalFilename,
            path = "${storage.path}/${multipartFile.originalFilename}",
            type = multipartFile.contentType,
            size = multipartFile.size,
            createdBy = "system"
        )
    }

    fun getPhysicalFileOnDiskByFileEntityId(id: Long): ByteArray {
        logger.info("Getting physical file on disk by file entity id: $id")

        val file = fileRepository.findById(id).orElseThrow { throw RuntimeException("File not found") }

        val filePath = Paths.get("${storage.path}/${file.hashKey}")

        return Files.readAllBytes(filePath)
    }
}
