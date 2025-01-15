package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.repository.FileRepository
import com.clangengineer.surveymodus.service.dto.FileDTO
import com.clangengineer.surveymodus.service.mapper.FileMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths

@Service
@Transactional
class MultipartFileService(
    private val fileMapper: FileMapper,
    private val fileRepository: FileRepository
) {
    private val logger = LoggerFactory.getLogger(MultipartFileService::class.java)

    fun saveMultipartFile(multipartFile: MultipartFile): FileDTO {
        logger.info("Saving file: ${multipartFile.originalFilename}")

        val fileDTO = FileDTO(
            filename = multipartFile.originalFilename,
            filepath = "./doc",
            createdBy = "system"
        )

        // save file
        val file = fileMapper.toEntity(fileDTO)
        fileRepository.save(file)

        val uploadPath = fileDTO.filepath?.let { Paths.get(it) } ?: Paths.get("./doc")
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }

        val hashedFileName = getSHA512(file.id.toString())
        val targetPath = uploadPath.resolve(hashedFileName)

        multipartFile.inputStream.use { inputStream ->
            Files.copy(inputStream, targetPath)
        }

        return fileMapper.toDto(file)
    }
}
