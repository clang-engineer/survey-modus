package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.service.dto.FileDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile
import java.nio.file.Files
import java.nio.file.Paths

class MultipartFileServiceTest {
    @Test
    fun `save multifile on disk`() {
        val mockMultipartFile =
            MockMultipartFile("file", "test.txt", "text/plain", "test data".toByteArray())

        val multipartFileService = MultipartFileService()

        val fileDTO =
            FileDTO(id = 999L, filename = "test.txt", filepath = "./doc/files", createdBy = null)

        multipartFileService.saveMultipartFile(fileDTO, mockMultipartFile)

        val savedFilePath = "./doc/files/" + getSHA512(fileDTO.id.toString())

        assertThat(Files.exists(Paths.get(savedFilePath))).isTrue
    }
}
