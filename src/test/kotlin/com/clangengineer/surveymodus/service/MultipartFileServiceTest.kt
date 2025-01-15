package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.service.dto.FileDTO
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile

class MultipartFileServiceTest {
    @Test
    fun `save multifile on disk`() {
        val mockMultipartFile =
            MockMultipartFile("file", "test.txt", "text/plain", "test data".toByteArray())

        val multipartFileService = MultipartFileService()

        val fileDTO = FileDTO(id = 999L, filename = "test.txt", filepath = "./doc/files", createdBy = null)

        multipartFileService.saveMultipartFile(fileDTO, mockMultipartFile)
    }
}
