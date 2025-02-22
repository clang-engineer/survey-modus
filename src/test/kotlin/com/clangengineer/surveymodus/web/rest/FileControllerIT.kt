import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.repository.FileRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var fileRepository: FileRepository

    @Test
    @Transactional
    @Throws(Exception::class)
    fun `test save entity and create multipart files on server`() {
        val databaseSizeBeforeCreate = fileRepository.findAll().size

        val list = mutableListOf<MockMultipartFile>()

        for (i in 0..2) {
            list.add(
                MockMultipartFile(
                    "multipartFiles",
                    "test$i.txt",
                    "text/plain",
                    "test data".toByteArray()
                )
            )
        }

        mockMvc.perform(
            multipart("/api/files/upload")
                .file(list[0])
                .file(list[1])
                .file(list[2])
                .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().isCreated)

        val databaseSizeAfterCreate = fileRepository.findAll().size
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 3)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun `test download exist file from server`() {
        val list = mutableListOf<MockMultipartFile>()

        for (i in 0..2) {
            list.add(
                MockMultipartFile(
                    "multipartFiles",
                    "test$i.txt",
                    "text/plain",
                    "test data".toByteArray()
                )
            )
        }

        mockMvc.perform(
            multipart("/api/files/upload")
                .file(list[0])
                .file(list[1])
                .file(list[2])
                .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().isCreated)

        val file = fileRepository.findAll().first()

        val mockResponse = mockMvc.perform(
            get("/api/files/download")
                .param("fileId", file.id.toString())
        ).andExpect(status().isOk)

        val content = mockResponse.andReturn().response.contentAsByteArray
        assertThat(content).isNotEmpty
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun `test download not exist file from server`() {
        mockMvc.perform(
            get("/api/files/download")
                .param("fileId", "1")
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.title").value("File not found"))
            .andExpect(jsonPath("$.entityName").value("file"))
            .andExpect(jsonPath("$.errorKey").value("notfound"))
            .andExpect(jsonPath("$.message").value("error.notfound"))
    }
}
