import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.config.ApplicationProperties
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var fileRepository: FileRepository

    @Autowired
    private lateinit var applicationProperties: ApplicationProperties

    @Test
    @Transactional
    @Throws(Exception::class)
    fun `test save entity and create multipart file on server`() {
        val databaseSizeBeforeCreate = fileRepository.findAll().size

        val mockMultipartFile = MockMultipartFile("multipartFile", "test.txt", "text/plain", "test data".toByteArray())

        mockMvc.perform(
            multipart("/api/files/upload")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().isCreated)

        val databaseSizeAfterCreate = fileRepository.findAll().size
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1)
    }
}
