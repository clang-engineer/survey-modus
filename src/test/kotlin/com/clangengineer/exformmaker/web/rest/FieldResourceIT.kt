package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.IntegrationTest
import com.clangengineer.exformmaker.domain.Field
import com.clangengineer.exformmaker.domain.Form
import com.clangengineer.exformmaker.domain.embeddable.FieldAttribute
import com.clangengineer.exformmaker.domain.embeddable.FieldDisplay
import com.clangengineer.exformmaker.domain.enumeration.type
import com.clangengineer.exformmaker.repository.FieldRepository
import com.clangengineer.exformmaker.service.mapper.FieldMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.EntityManager
import kotlin.test.assertNotNull

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FieldResourceIT {
    @Autowired
    private lateinit var fieldRepository: FieldRepository

    @Autowired
    private lateinit var fieldMapper: FieldMapper

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restFieldMockMvc: MockMvc

    private lateinit var field: Field

    @BeforeEach
    fun initTest() {
        field = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createField() {
        val databaseSizeBeforeCreate = fieldRepository.findAll().size

        val fieldDTO = fieldMapper.toDto(field)
        restFieldMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fieldDTO))
        ).andExpect(status().isCreated)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate + 1)
        val testField = fieldList[fieldList.size - 1]

        assertThat(testField.title).isEqualTo(DEFAULT_TITLE)
        assertThat(testField.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testField.activated).isEqualTo(DEFAULT_ACTIVATED)

        assertThat(testField.attribute).isEqualTo(DEFAULT_ATTRIBUTE)
        assertThat(testField.display).isEqualTo(DEFAULT_DISPLAY)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createFieldWithExistingId() {
        field.id = 1L
        val fieldDTO = fieldMapper.toDto(field)

        val databaseSizeBeforeCreate = fieldRepository.findAll().size

        restFieldMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fieldDTO))
        ).andExpect(status().isBadRequest)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkTitleIsRequired() {
        val databaseSizeBeforeTest = fieldRepository.findAll().size

        field.title = null

        val fieldDTO = fieldMapper.toDto(field)

        restFieldMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fieldDTO))
        ).andExpect(status().isBadRequest)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFields() {
        fieldRepository.saveAndFlush(field)

        restFieldMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(field.id?.toInt())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED)))
            .andExpect(jsonPath("$.[*].attribute.type").value(hasItem(DEFAULT_ATTRIBUTE.type?.name)))
            .andExpect(jsonPath("$.[*].display.orderNo").value(hasItem(DEFAULT_DISPLAY.orderNo)))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getField() {
        fieldRepository.saveAndFlush(field)

        val id = field.id
        assertNotNull(id)

        restFieldMockMvc.perform(get(ENTITY_API_URL_ID, field.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(field.id?.toInt()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED))
            .andExpect(jsonPath("$.attribute").value(DEFAULT_ATTRIBUTE))
            .andExpect(jsonPath("$.display").value(DEFAULT_DISPLAY))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getFieldsByIdFiltering() {
        fieldRepository.saveAndFlush(field)
        val id = field.id

        defaultFieldShouldBeFound("id.equals=$id")
        defaultFieldShouldNotBeFound("id.notEquals=$id")

        defaultFieldShouldBeFound("id.greaterThanOrEqual=$id")
        defaultFieldShouldNotBeFound("id.greaterThan=$id")

        defaultFieldShouldBeFound("id.lessThanOrEqual=$id")
        defaultFieldShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFieldsByTitleIsEqualToSomething() {
        fieldRepository.saveAndFlush(field)

        defaultFieldShouldBeFound("title.equals=$DEFAULT_TITLE")

        defaultFieldShouldNotBeFound("title.equals=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFieldsByTitleIsInShouldWork() {
        fieldRepository.saveAndFlush(field)

        defaultFieldShouldBeFound("title.in=$DEFAULT_TITLE,$UPDATED_TITLE")

        defaultFieldShouldNotBeFound("title.in=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFieldsByTitleIsNullOrNotNull() {
        fieldRepository.saveAndFlush(field)

        defaultFieldShouldBeFound("title.specified=true")

        defaultFieldShouldNotBeFound("title.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFieldsByTitleContainsSomething() {
        fieldRepository.saveAndFlush(field)

        defaultFieldShouldBeFound("title.contains=$DEFAULT_TITLE")

        defaultFieldShouldNotBeFound("title.contains=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFieldsByTitleNotContainsSomething() {
        fieldRepository.saveAndFlush(field)

        defaultFieldShouldNotBeFound("title.doesNotContain=$DEFAULT_TITLE")

        defaultFieldShouldBeFound("title.doesNotContain=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFieldsByDescriptionIsEqualToSomething() {
        fieldRepository.saveAndFlush(field)

        defaultFieldShouldBeFound("description.equals=$DEFAULT_DESCRIPTION")

        defaultFieldShouldNotBeFound("description.equals=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFieldsByDescriptionIsInShouldWork() {
        fieldRepository.saveAndFlush(field)

        defaultFieldShouldBeFound("description.in=$DEFAULT_DESCRIPTION,$UPDATED_DESCRIPTION")

        defaultFieldShouldNotBeFound("description.in=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFieldsByDescriptionIsNullOrNotNull() {
        fieldRepository.saveAndFlush(field)

        defaultFieldShouldBeFound("description.specified=true")

        defaultFieldShouldNotBeFound("description.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFieldsByDescriptionContainsSomething() {
        fieldRepository.saveAndFlush(field)

        defaultFieldShouldBeFound("description.contains=$DEFAULT_DESCRIPTION")

        defaultFieldShouldNotBeFound("description.contains=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFieldsByDescriptionNotContainsSomething() {
        fieldRepository.saveAndFlush(field)

        defaultFieldShouldNotBeFound("description.doesNotContain=$DEFAULT_DESCRIPTION")

        defaultFieldShouldBeFound("description.doesNotContain=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFieldsByActivatedIsEqualToSomething() {
        fieldRepository.saveAndFlush(field)

        defaultFieldShouldBeFound("activated.equals=$DEFAULT_ACTIVATED")

        defaultFieldShouldNotBeFound("activated.equals=$UPDATED_ACTIVATED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFieldsByActivatedIsInShouldWork() {
        fieldRepository.saveAndFlush(field)

        defaultFieldShouldBeFound("activated.in=$DEFAULT_ACTIVATED,$UPDATED_ACTIVATED")

        defaultFieldShouldNotBeFound("activated.in=$UPDATED_ACTIVATED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFieldsByActivatedIsNullOrNotNull() {
        fieldRepository.saveAndFlush(field)

        defaultFieldShouldBeFound("activated.specified=true")

        defaultFieldShouldNotBeFound("activated.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFieldsByFormIsEqualToSomething() {
        var form: Form
        if (findAll(em, Form::class).isEmpty()) {
            fieldRepository.saveAndFlush(field)
            form = FormResourceIT.createEntity(em)
        } else {
            form = findAll(em, Form::class)[0]
        }
        em.persist(form)
        em.flush()
        field.form = form
        fieldRepository.saveAndFlush(field)
        val formId = form?.id

        defaultFieldShouldBeFound("formId.equals=$formId")

        defaultFieldShouldNotBeFound("formId.equals=${(formId?.plus(1))}")
    }

    @Throws(Exception::class)
    private fun defaultFieldShouldBeFound(filter: String) {
        restFieldMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(field.id?.toInt())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED)))
            .andExpect(jsonPath("$.[*].attribute.type").value(hasItem(DEFAULT_ATTRIBUTE.type?.name)))
            .andExpect(jsonPath("$.[*].attribute.defaultValue").value(hasItem(DEFAULT_ATTRIBUTE.defaultValue)))
            .andExpect(jsonPath("$.[*].display.orderNo").value(hasItem(DEFAULT_DISPLAY.orderNo)))

        restFieldMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    @Throws(Exception::class)
    private fun defaultFieldShouldNotBeFound(filter: String) {
        restFieldMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        restFieldMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingField() {
        restFieldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun putExistingField() {
        fieldRepository.saveAndFlush(field)

        val databaseSizeBeforeUpdate = fieldRepository.findAll().size

        val updatedField = fieldRepository.findById(field.id).get()
        em.detach(updatedField)
        updatedField.title = UPDATED_TITLE
        updatedField.description = UPDATED_DESCRIPTION
        updatedField.activated = UPDATED_ACTIVATED
        updatedField.attribute = UPDATED_ATTRIBUTE
        updatedField.display = UPDATED_DISPLAY
        val fieldDTO = fieldMapper.toDto(updatedField)

        restFieldMockMvc.perform(
            put(ENTITY_API_URL_ID, fieldDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fieldDTO))
        ).andExpect(status().isOk)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate)
        val testField = fieldList[fieldList.size - 1]
        assertThat(testField.title).isEqualTo(UPDATED_TITLE)
        assertThat(testField.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testField.activated).isEqualTo(UPDATED_ACTIVATED)
        assertThat(testField.attribute).isEqualTo(UPDATED_ATTRIBUTE)
        assertThat(testField.display).isEqualTo(UPDATED_DISPLAY)
    }

    @Test
    @Transactional
    fun putNonExistingField() {
        val databaseSizeBeforeUpdate = fieldRepository.findAll().size
        field.id = count.incrementAndGet()

        val fieldDTO = fieldMapper.toDto(field)

        restFieldMockMvc.perform(
            put(ENTITY_API_URL_ID, fieldDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fieldDTO))
        )
            .andExpect(status().isBadRequest)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchField() {
        val databaseSizeBeforeUpdate = fieldRepository.findAll().size
        field.id = count.incrementAndGet()

        val fieldDTO = fieldMapper.toDto(field)

        restFieldMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fieldDTO))
        )
            .andExpect(status().isBadRequest)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamField() {
        val databaseSizeBeforeUpdate = fieldRepository.findAll().size
        field.id = count.incrementAndGet()

        val fieldDTO = fieldMapper.toDto(field)

        restFieldMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fieldDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateFieldWithPatch() {
        fieldRepository.saveAndFlush(field)

        val databaseSizeBeforeUpdate = fieldRepository.findAll().size

        val partialUpdatedField = Field().apply {
            id = field.id
            title = UPDATED_TITLE
            description = UPDATED_DESCRIPTION
            activated = UPDATED_ACTIVATED
        }

        restFieldMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedField.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedField))
        )
            .andExpect(status().isOk)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate)
        val testField = fieldList.last()
        assertThat(testField.title).isEqualTo(UPDATED_TITLE)
        assertThat(testField.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testField.activated).isEqualTo(UPDATED_ACTIVATED)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateFieldWithPatch() {
        fieldRepository.saveAndFlush(field)

        val databaseSizeBeforeUpdate = fieldRepository.findAll().size

        val partialUpdatedField = Field().apply {
            id = field.id
            title = UPDATED_TITLE
            description = UPDATED_DESCRIPTION
            activated = UPDATED_ACTIVATED
            attribute = UPDATED_ATTRIBUTE
            display = UPDATED_DISPLAY
        }

        restFieldMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedField.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedField))
        )
            .andExpect(status().isOk)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate)
        val testField = fieldList.last()
        assertThat(testField.title).isEqualTo(UPDATED_TITLE)
        assertThat(testField.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testField.activated).isEqualTo(UPDATED_ACTIVATED)
        assertThat(testField.attribute).isEqualTo(UPDATED_ATTRIBUTE)
        assertThat(testField.display).isEqualTo(UPDATED_DISPLAY)
    }

    @Throws(Exception::class)
    fun patchNonExistingField() {
        val databaseSizeBeforeUpdate = fieldRepository.findAll().size
        field.id = count.incrementAndGet()

        val fieldDTO = fieldMapper.toDto(field)

        restFieldMockMvc.perform(
            patch(ENTITY_API_URL_ID, fieldDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(fieldDTO))
        )
            .andExpect(status().isBadRequest)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchField() {
        val databaseSizeBeforeUpdate = fieldRepository.findAll().size
        field.id = count.incrementAndGet()

        val fieldDTO = fieldMapper.toDto(field)

        restFieldMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(fieldDTO))
        )
            .andExpect(status().isBadRequest)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamField() {
        val databaseSizeBeforeUpdate = fieldRepository.findAll().size
        field.id = count.incrementAndGet()

        val fieldDTO = fieldMapper.toDto(field)

        restFieldMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(fieldDTO))
        ).andExpect(status().isMethodNotAllowed)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteField() {
        fieldRepository.saveAndFlush(field)
        val databaseSizeBeforeDelete = fieldRepository.findAll().size

        restFieldMockMvc.perform(
            delete(ENTITY_API_URL_ID, field.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_TITLE =
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        private const val UPDATED_TITLE =
            "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB"

        private const val DEFAULT_DESCRIPTION = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        private const val DEFAULT_ACTIVATED: Boolean = false
        private const val UPDATED_ACTIVATED: Boolean = true

        private val DEFAULT_ATTRIBUTE: FieldAttribute = FieldAttribute(type.TEXT, "AAAAAAAAAA")
        private val UPDATED_ATTRIBUTE: FieldAttribute = FieldAttribute(type.INTEGER, "BBBBBBBBBB")

        private val DEFAULT_DISPLAY: FieldDisplay = FieldDisplay(1)
        private val UPDATED_DISPLAY: FieldDisplay = FieldDisplay(2)

        private val ENTITY_API_URL: String = "/api/fields"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong =
            AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

        @JvmStatic
        fun createEntity(em: EntityManager): Field {
            val field = Field(
                title = DEFAULT_TITLE,
                description = DEFAULT_DESCRIPTION,
                activated = DEFAULT_ACTIVATED,
                attribute = DEFAULT_ATTRIBUTE,
                display = DEFAULT_DISPLAY
            )

            val form = FormResourceIT.createEntity(em)
            em.persist(form)
            em.flush()
            field.form = form
            return field
        }

        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Field {
            val field = Field(
                title = UPDATED_TITLE,
                description = UPDATED_DESCRIPTION,
                activated = UPDATED_ACTIVATED,
                attribute = UPDATED_ATTRIBUTE,
                display = UPDATED_DISPLAY
            )

            val form = FormResourceIT.createEntity(em)
            em.persist(form)
            em.flush()
            field.form = form
            return field
        }
    }
}
