package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.domain.File
import com.clangengineer.surveymodus.repository.FileRepository
import com.clangengineer.surveymodus.service.mapper.FileMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.EntityManager
import kotlin.test.assertNotNull

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileResourceIT {
    @Autowired
    private lateinit var fileRepository: FileRepository

    @Autowired
    private lateinit var fileMapper: FileMapper

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restFileMockMvc: MockMvc

    private lateinit var file: File

    @BeforeEach
    fun initTest() {
        file = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createFile() {
        val databaseSizeBeforeCreate = fileRepository.findAll().size
        // Create the File
        val fileDTO = fileMapper.toDto(file)
        restFileMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileDTO))
        ).andExpect(status().isCreated)

        // Validate the File in the database
        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeCreate + 1)
        val testFile = fileList[fileList.size - 1]

        assertThat(testFile.name).isEqualTo(DEFAULT_NAME)
        assertThat(testFile.path).isEqualTo(DEFAULT_PATH)
        assertThat(testFile.createdBy).isEqualTo(DEFAULT_CREATED_BY)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createFileWithExistingId() {
        file.id = 1L
        val fileDTO = fileMapper.toDto(file)

        val databaseSizeBeforeCreate = fileRepository.findAll().size

        restFileMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileDTO))
        ).andExpect(status().isBadRequest)

        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkTitleIsRequired() {
        val databaseSizeBeforeTest = fileRepository.findAll().size

        file.name = null

        val fileDTO = fileMapper.toDto(file)

        restFileMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileDTO))
        ).andExpect(status().isBadRequest)

        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFiles() {

        fileRepository.saveAndFlush(file)

        restFileMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(file.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getFile() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        val id = file.id
        assertNotNull(id)

        // Get the file
        restFileMockMvc.perform(get(ENTITY_API_URL_ID, file.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(file.id?.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getFilesByIdFiltering() {
        fileRepository.saveAndFlush(file)
        val id = file.id

        defaultFileShouldBeFound("id.equals=$id")
        defaultFileShouldNotBeFound("id.notEquals=$id")
        defaultFileShouldBeFound("id.greaterThanOrEqual=$id")
        defaultFileShouldNotBeFound("id.greaterThan=$id")

        defaultFileShouldBeFound("id.lessThanOrEqual=$id")
        defaultFileShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByTitleIsEqualToSomething() {
        fileRepository.saveAndFlush(file)

        defaultFileShouldBeFound("name.equals=$DEFAULT_NAME")
        defaultFileShouldNotBeFound("name.equals=$UPDATED_FILENAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByTitleIsInShouldWork() {
        fileRepository.saveAndFlush(file)

        defaultFileShouldBeFound("name.in=$DEFAULT_NAME,$UPDATED_FILENAME")
        defaultFileShouldNotBeFound("name.in=$UPDATED_FILENAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByTitleIsNullOrNotNull() {
        fileRepository.saveAndFlush(file)

        defaultFileShouldBeFound("name.specified=true")
        defaultFileShouldNotBeFound("name.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByTitleContainsSomething() {
        fileRepository.saveAndFlush(file)

        defaultFileShouldBeFound("name.contains=$DEFAULT_NAME")
        defaultFileShouldNotBeFound("name.contains=$UPDATED_FILENAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByTitleNotContainsSomething() {
        fileRepository.saveAndFlush(file)

        defaultFileShouldNotBeFound("name.doesNotContain=$DEFAULT_NAME")
        defaultFileShouldBeFound("name.doesNotContain=$UPDATED_FILENAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByDescriptionIsEqualToSomething() {
        fileRepository.saveAndFlush(file)

        defaultFileShouldBeFound("path.equals=$DEFAULT_PATH")
        defaultFileShouldNotBeFound("path.equals=$UPDATED_FILEPATH")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByDescriptionIsInShouldWork() {
        fileRepository.saveAndFlush(file)

        defaultFileShouldBeFound("path.in=$DEFAULT_PATH,$UPDATED_FILEPATH")
        defaultFileShouldNotBeFound("path.in=$UPDATED_FILEPATH")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByDescriptionIsNullOrNotNull() {
        fileRepository.saveAndFlush(file)

        defaultFileShouldBeFound("path.specified=true")
        defaultFileShouldNotBeFound("path.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByDescriptionContainsSomething() {
        fileRepository.saveAndFlush(file)

        defaultFileShouldBeFound("path.contains=$DEFAULT_PATH")
        defaultFileShouldNotBeFound("path.contains=$UPDATED_FILEPATH")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByDescriptionNotContainsSomething() {
        fileRepository.saveAndFlush(file)

        defaultFileShouldNotBeFound("path.doesNotContain=$DEFAULT_PATH")
        defaultFileShouldBeFound("path.doesNotContain=$UPDATED_FILEPATH")
    }

    @Throws(Exception::class)
    private fun defaultFileShouldBeFound(filter: String) {
        restFileMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(file.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))

        restFileMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    @Throws(Exception::class)
    private fun defaultFileShouldNotBeFound(filter: String) {
        restFileMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        restFileMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingFile() {
        restFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun putExistingFile() {
        fileRepository.saveAndFlush(file)

        val databaseSizeBeforeUpdate = fileRepository.findAll().size

        val updatedFile = fileRepository.findById(file.id).get()
        em.detach(updatedFile)
        updatedFile.name = UPDATED_FILENAME
        updatedFile.path = UPDATED_FILEPATH
        updatedFile.createdBy = UPDATED_CREATED_BY

        val fileDTO = fileMapper.toDto(updatedFile)

        restFileMockMvc.perform(
            put(ENTITY_API_URL_ID, fileDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileDTO))
        ).andExpect(status().isOk)

        // Validate the File in the database
        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
        val testFile = fileList[fileList.size - 1]
        assertThat(testFile.name).isEqualTo(UPDATED_FILENAME)
        assertThat(testFile.path).isEqualTo(UPDATED_FILEPATH)
    }

    @Test
    @Transactional
    fun putNonExistingFile() {
        val databaseSizeBeforeUpdate = fileRepository.findAll().size
        file.id = count.incrementAndGet()

        val fileDTO = fileMapper.toDto(file)

        restFileMockMvc.perform(
            put(ENTITY_API_URL_ID, fileDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileDTO))
        ).andExpect(status().isBadRequest)

        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchFile() {
        val databaseSizeBeforeUpdate = fileRepository.findAll().size
        file.id = count.incrementAndGet()

        val fileDTO = fileMapper.toDto(file)

        restFileMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileDTO))
        ).andExpect(status().isBadRequest)

        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamFile() {
        val databaseSizeBeforeUpdate = fileRepository.findAll().size
        file.id = count.incrementAndGet()

        val fileDTO = fileMapper.toDto(file)

        restFileMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileDTO))
        ).andExpect(status().isMethodNotAllowed)

        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateFileWithPatch() {
        fileRepository.saveAndFlush(file)

        val databaseSizeBeforeUpdate = fileRepository.findAll().size

        val partialUpdatedFile = File().apply {
            id = file.id
            name = UPDATED_FILENAME
            path = UPDATED_FILEPATH
        }

        restFileMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedFile.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedFile))
        )
            .andExpect(status().isOk)

        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
        val testFile = fileList.last()
        assertThat(testFile.name).isEqualTo(UPDATED_FILENAME)
        assertThat(testFile.path).isEqualTo(UPDATED_FILEPATH)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateFileWithPatch() {
        fileRepository.saveAndFlush(file)

        val databaseSizeBeforeUpdate = fileRepository.findAll().size

        val partialUpdatedFile = File().apply {
            id = file.id
            name = UPDATED_FILENAME
            path = UPDATED_FILEPATH
        }

        restFileMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedFile.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedFile))
        ).andExpect(status().isOk)

        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
        val testFile = fileList.last()
        assertThat(testFile.name).isEqualTo(UPDATED_FILENAME)
        assertThat(testFile.path).isEqualTo(UPDATED_FILEPATH)
    }

    @Throws(Exception::class)
    fun patchNonExistingFile() {
        val databaseSizeBeforeUpdate = fileRepository.findAll().size
        file.id = count.incrementAndGet()

        val fileDTO = fileMapper.toDto(file)

        restFileMockMvc.perform(
            patch(ENTITY_API_URL_ID, fileDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(fileDTO))
        ).andExpect(status().isBadRequest)

        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchFile() {
        val databaseSizeBeforeUpdate = fileRepository.findAll().size
        file.id = count.incrementAndGet()

        val fileDTO = fileMapper.toDto(file)

        restFileMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(fileDTO))
        ).andExpect(status().isBadRequest)

        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamFile() {
        val databaseSizeBeforeUpdate = fileRepository.findAll().size
        file.id = count.incrementAndGet()

        val fileDTO = fileMapper.toDto(file)

        restFileMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(fileDTO))
        ).andExpect(status().isMethodNotAllowed)

        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteFile() {
        fileRepository.saveAndFlush(file)
        val databaseSizeBeforeDelete = fileRepository.findAll().size

        restFileMockMvc.perform(
            delete(ENTITY_API_URL_ID, file.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_NAME =
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        private const val UPDATED_FILENAME =
            "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB"

        private const val DEFAULT_PATH =
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        private const val UPDATED_FILEPATH =
            "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB"

        private const val DEFAULT_SIZE = 1L
        private const val UPDATED_SIZE = 2L

        private const val DEFAULT_TYPE = "AAAAAAAAAA"
        private const val UPDATED_TYPE = "BBBBBBBBBB"

        private const val DEFAULT_CREATED_BY = "system"
        private const val UPDATED_CREATED_BY = "system"

        private const val DEFAULT_CREATED_DATE = "2021-08-04T00:00:00Z"
        private const val UPDATED_LAST_MODIFIED_DATE = "2021-08-04T00:00:00Z"

        private val ENTITY_API_URL: String = "/api/files"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong =
            AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

        @JvmStatic
        fun createEntity(em: EntityManager): File {
            val file = File(
                name = DEFAULT_NAME,
                path = DEFAULT_PATH,
                size = DEFAULT_SIZE,
                type = DEFAULT_TYPE,
                createdBy = DEFAULT_CREATED_BY,
                createdDate = Instant.now()
            )

            return file
        }

        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): File {
            val file = File(
                name = UPDATED_FILENAME,
                path = UPDATED_FILEPATH,
                size = UPDATED_SIZE,
                type = UPDATED_TYPE,
                createdBy = DEFAULT_CREATED_BY,
                createdDate = Instant.now()
            )

            return file
        }
    }
}
