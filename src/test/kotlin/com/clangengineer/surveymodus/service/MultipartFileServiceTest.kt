package com.clangengineer.surveymodus.service

import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile

class MultipartFileServiceTest {
    @Test
    fun `save multifile on disk`() {
        val mockMultipartFile = MockMultipartFile("file", "test.txt", "text/plain", "test data".toByteArray())

        val multipartFileService = MultipartFileService()
        multipartFileService.saveMultipartFile(mockMultipartFile)
    }
}
