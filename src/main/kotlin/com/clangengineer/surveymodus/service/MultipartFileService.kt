package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.service.dto.FileDTO
import org.slf4j.LoggerFactory
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths

class MultipartFileService {
    private val logger = LoggerFactory.getLogger(MultipartFileService::class.java)

    fun saveMultipartFile(fileDTO: FileDTO, multipartFile: MultipartFile) {
        logger.info("Saving file: ${multipartFile.originalFilename}")

        val uploadPath = fileDTO.filepath?.let { Paths.get(it) } ?: Paths.get("./doc")
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }

        val hashedFileName = getSHA512(fileDTO.id.toString())
        val targetPath = uploadPath.resolve(hashedFileName)

        multipartFile.inputStream.use { inputStream ->
            Files.copy(inputStream, targetPath)
        }
    }
}
