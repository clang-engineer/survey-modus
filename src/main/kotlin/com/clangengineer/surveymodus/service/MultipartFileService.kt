package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.service.dto.FileDTO
import org.slf4j.LoggerFactory
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

class MultipartFileService {
    private final val logger = LoggerFactory.getLogger(MultipartFileService::class.java)

    fun saveMultipartFile(
        fileDTO: FileDTO,
        multipartFile: MultipartFile) {
        logger.info("Saving file: ${multipartFile.originalFilename}")

        val uploadPath = Paths.get("uploads") // 업로드 디렉터리
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath) // 디렉터리 생성
        }

        // 2. 파일 이름 처리 (UUID 기반)
        val originalFileName = multipartFile.originalFilename ?: "unknown"
        val fileExtension = originalFileName.substringAfterLast(".", "")
        val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"

        // 3. 파일 저장 경로
        val targetPath = uploadPath.resolve(uniqueFileName)

        // 4. 파일 저장
        multipartFile.inputStream.use { inputStream ->
            Files.copy(inputStream, targetPath)
        }
    }
}
