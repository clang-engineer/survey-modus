package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.domain.Form
import com.clangengineer.surveymodus.domain.User
import com.clangengineer.surveymodus.domain.enumeration.level
import com.clangengineer.surveymodus.repository.FormRepository
import com.clangengineer.surveymodus.service.mapper.FormMapper
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
class FormResourceIT {
    @Autowired
    private lateinit var formRepository: FormRepository

    @Autowired
    private lateinit var formMapper: FormMapper

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restFormMockMvc: MockMvc

    private lateinit var form: Form

    @BeforeEach
    fun initTest() {
        form = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createForm() {
        val databaseSizeBeforeCreate = formRepository.findAll().size

        val formDTO = formMapper.toDto(form)
        restFormMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(formDTO))
        ).andExpect(status().isCreated)

        val formList = formRepository.findAll()
        assertThat(formList).hasSize(databaseSizeBeforeCreate + 1)
        val testForm = formList[formList.size - 1]

        assertThat(testForm.title).isEqualTo(DEFAULT_TITLE)
        assertThat(testForm.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testForm.activated).isEqualTo(DEFAULT_ACTIVATED)
        assertThat(testForm.orderNo).isEqualTo(DEFAULT_ORDER_NO)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createFormWithExistingId() {
        form.id = 1L
        val formDTO = formMapper.toDto(form)

        val databaseSizeBeforeCreate = formRepository.findAll().size

        restFormMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(formDTO))
        ).andExpect(status().isBadRequest)

        val formList = formRepository.findAll()
        assertThat(formList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkTitleIsRequired() {
        val databaseSizeBeforeTest = formRepository.findAll().size

        form.title = null

        val formDTO = formMapper.toDto(form)

        restFormMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(formDTO))
        ).andExpect(status().isBadRequest)

        val formList = formRepository.findAll()
        assertThat(formList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllForms() {
        formRepository.saveAndFlush(form)

        restFormMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(form.id?.toInt())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED)))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO)))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getForm() {
        formRepository.saveAndFlush(form)

        val id = form.id
        assertNotNull(id)

        restFormMockMvc.perform(get(ENTITY_API_URL_ID, form.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(form.id?.toInt()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED))
            .andExpect(jsonPath("$.orderNo").value(DEFAULT_ORDER_NO))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getFormsByIdFiltering() {
        formRepository.saveAndFlush(form)
        val id = form.id

        defaultFormShouldBeFound("id.equals=$id")
        defaultFormShouldNotBeFound("id.notEquals=$id")

        defaultFormShouldBeFound("id.greaterThanOrEqual=$id")
        defaultFormShouldNotBeFound("id.greaterThan=$id")

        defaultFormShouldBeFound("id.lessThanOrEqual=$id")
        defaultFormShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFormsByTitleIsEqualToSomething() {
        formRepository.saveAndFlush(form)

        defaultFormShouldBeFound("title.equals=$DEFAULT_TITLE")

        defaultFormShouldNotBeFound("title.equals=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFormsByTitleIsInShouldWork() {
        formRepository.saveAndFlush(form)

        defaultFormShouldBeFound("title.in=$DEFAULT_TITLE,$UPDATED_TITLE")

        defaultFormShouldNotBeFound("title.in=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFormsByTitleIsNullOrNotNull() {
        formRepository.saveAndFlush(form)

        defaultFormShouldBeFound("title.specified=true")

        defaultFormShouldNotBeFound("title.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFormsByTitleContainsSomething() {
        formRepository.saveAndFlush(form)

        defaultFormShouldBeFound("title.contains=$DEFAULT_TITLE")

        defaultFormShouldNotBeFound("title.contains=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFormsByTitleNotContainsSomething() {
        formRepository.saveAndFlush(form)

        defaultFormShouldNotBeFound("title.doesNotContain=$DEFAULT_TITLE")

        defaultFormShouldBeFound("title.doesNotContain=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFormsByDescriptionIsEqualToSomething() {
        formRepository.saveAndFlush(form)

        defaultFormShouldBeFound("description.equals=$DEFAULT_DESCRIPTION")

        defaultFormShouldNotBeFound("description.equals=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFormsByDescriptionIsInShouldWork() {
        formRepository.saveAndFlush(form)

        defaultFormShouldBeFound("description.in=$DEFAULT_DESCRIPTION,$UPDATED_DESCRIPTION")

        defaultFormShouldNotBeFound("description.in=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFormsByDescriptionIsNullOrNotNull() {
        formRepository.saveAndFlush(form)

        defaultFormShouldBeFound("description.specified=true")

        defaultFormShouldNotBeFound("description.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFormsByDescriptionContainsSomething() {
        formRepository.saveAndFlush(form)

        defaultFormShouldBeFound("description.contains=$DEFAULT_DESCRIPTION")

        defaultFormShouldNotBeFound("description.contains=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFormsByDescriptionNotContainsSomething() {
        formRepository.saveAndFlush(form)

        defaultFormShouldNotBeFound("description.doesNotContain=$DEFAULT_DESCRIPTION")

        defaultFormShouldBeFound("description.doesNotContain=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFormsByActivatedIsEqualToSomething() {
        formRepository.saveAndFlush(form)

        defaultFormShouldBeFound("activated.equals=$DEFAULT_ACTIVATED")

        defaultFormShouldNotBeFound("activated.equals=$UPDATED_ACTIVATED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFormsByActivatedIsInShouldWork() {
        formRepository.saveAndFlush(form)

        defaultFormShouldBeFound("activated.in=$DEFAULT_ACTIVATED,$UPDATED_ACTIVATED")

        defaultFormShouldNotBeFound("activated.in=$UPDATED_ACTIVATED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFormsByActivatedIsNullOrNotNull() {
        formRepository.saveAndFlush(form)

        defaultFormShouldBeFound("activated.specified=true")

        defaultFormShouldNotBeFound("activated.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFormsByUserIsEqualToSomething() {
        var user: User
        if (findAll(em, User::class).isEmpty()) {
            formRepository.saveAndFlush(form)
            user = UserResourceIT.createEntity(em)
        } else {
            user = findAll(em, User::class)[0]
        }
        em.persist(user)
        em.flush()
        form.user = user
        formRepository.saveAndFlush(form)
        val userId = user?.id

        defaultFormShouldBeFound("userId.equals=$userId")

        defaultFormShouldNotBeFound("userId.equals=${(userId?.plus(1))}")
    }

    @Throws(Exception::class)
    private fun defaultFormShouldBeFound(filter: String) {
        restFormMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(form.id?.toInt())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED)))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO)))

        restFormMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    @Throws(Exception::class)
    private fun defaultFormShouldNotBeFound(filter: String) {
        restFormMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        restFormMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingForm() {
        restFormMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun putExistingForm() {
        formRepository.saveAndFlush(form)

        val databaseSizeBeforeUpdate = formRepository.findAll().size

        val updatedForm = formRepository.findById(form.id).get()
        em.detach(updatedForm)
        updatedForm.title = UPDATED_TITLE
        updatedForm.description = UPDATED_DESCRIPTION
        updatedForm.activated = UPDATED_ACTIVATED
        updatedForm.orderNo = UPDATED_ORDER_NO
        val formDTO = formMapper.toDto(updatedForm)

        restFormMockMvc.perform(
            put(ENTITY_API_URL_ID, formDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(formDTO))
        ).andExpect(status().isOk)

        val formList = formRepository.findAll()
        assertThat(formList).hasSize(databaseSizeBeforeUpdate)
        val testForm = formList[formList.size - 1]
        assertThat(testForm.title).isEqualTo(UPDATED_TITLE)
        assertThat(testForm.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testForm.activated).isEqualTo(UPDATED_ACTIVATED)
        assertThat(testForm.orderNo).isEqualTo(UPDATED_ORDER_NO)
    }

    @Test
    @Transactional
    fun putNonExistingForm() {
        val databaseSizeBeforeUpdate = formRepository.findAll().size
        form.id = count.incrementAndGet()

        val formDTO = formMapper.toDto(form)

        restFormMockMvc.perform(
            put(ENTITY_API_URL_ID, formDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(formDTO))
        )
            .andExpect(status().isBadRequest)

        val formList = formRepository.findAll()
        assertThat(formList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchForm() {
        val databaseSizeBeforeUpdate = formRepository.findAll().size
        form.id = count.incrementAndGet()

        val formDTO = formMapper.toDto(form)

        restFormMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(formDTO))
        )
            .andExpect(status().isBadRequest)

        val formList = formRepository.findAll()
        assertThat(formList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamForm() {
        val databaseSizeBeforeUpdate = formRepository.findAll().size
        form.id = count.incrementAndGet()

        val formDTO = formMapper.toDto(form)

        restFormMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(formDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        val formList = formRepository.findAll()
        assertThat(formList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateFormWithPatch() {
        formRepository.saveAndFlush(form)

        val databaseSizeBeforeUpdate = formRepository.findAll().size

        val partialUpdatedForm = Form().apply {
            id = form.id
            title = UPDATED_TITLE
            description = UPDATED_DESCRIPTION
            activated = UPDATED_ACTIVATED
            orderNo = UPDATED_ORDER_NO
        }

        restFormMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedForm.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedForm))
        )
            .andExpect(status().isOk)

        val formList = formRepository.findAll()
        assertThat(formList).hasSize(databaseSizeBeforeUpdate)
        val testForm = formList.last()
        assertThat(testForm.title).isEqualTo(UPDATED_TITLE)
        assertThat(testForm.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testForm.activated).isEqualTo(UPDATED_ACTIVATED)
        assertThat(testForm.orderNo).isEqualTo(UPDATED_ORDER_NO)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateFormWithPatch() {
        formRepository.saveAndFlush(form)

        val databaseSizeBeforeUpdate = formRepository.findAll().size

        val partialUpdatedForm = Form().apply {
            id = form.id
            title = UPDATED_TITLE
            description = UPDATED_DESCRIPTION
            activated = UPDATED_ACTIVATED
        }

        restFormMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedForm.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedForm))
        )
            .andExpect(status().isOk)

        val formList = formRepository.findAll()
        assertThat(formList).hasSize(databaseSizeBeforeUpdate)
        val testForm = formList.last()
        assertThat(testForm.title).isEqualTo(UPDATED_TITLE)
        assertThat(testForm.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testForm.activated).isEqualTo(UPDATED_ACTIVATED)
        assertThat(testForm.orderNo).isEqualTo(UPDATED_ORDER_NO)
    }

    @Throws(Exception::class)
    fun patchNonExistingForm() {
        val databaseSizeBeforeUpdate = formRepository.findAll().size
        form.id = count.incrementAndGet()

        val formDTO = formMapper.toDto(form)

        restFormMockMvc.perform(
            patch(ENTITY_API_URL_ID, formDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(formDTO))
        )
            .andExpect(status().isBadRequest)

        val formList = formRepository.findAll()
        assertThat(formList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchForm() {
        val databaseSizeBeforeUpdate = formRepository.findAll().size
        form.id = count.incrementAndGet()

        val formDTO = formMapper.toDto(form)

        restFormMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(formDTO))
        )
            .andExpect(status().isBadRequest)

        val formList = formRepository.findAll()
        assertThat(formList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamForm() {
        val databaseSizeBeforeUpdate = formRepository.findAll().size
        form.id = count.incrementAndGet()

        val formDTO = formMapper.toDto(form)

        restFormMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(formDTO))
        ).andExpect(status().isMethodNotAllowed)

        val formList = formRepository.findAll()
        assertThat(formList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteForm() {
        formRepository.saveAndFlush(form)
        val databaseSizeBeforeDelete = formRepository.findAll().size

        restFormMockMvc.perform(
            delete(ENTITY_API_URL_ID, form.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        val formList = formRepository.findAll()
        assertThat(formList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_TITLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        private const val UPDATED_TITLE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB"

        private const val DEFAULT_DESCRIPTION = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        private const val DEFAULT_ACTIVATED: Boolean = false
        private const val UPDATED_ACTIVATED: Boolean = true

        private const val DEFAULT_ORDER_NO: Int = 1
        private const val UPDATED_ORDER_NO: Int = 1

        private val DEFAULT_TYPE: level = level.EASY
        private val UPDATED_TYPE: level = level.NORMAL

        private val ENTITY_API_URL: String = "/api/forms"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

        @JvmStatic
        fun createEntity(em: EntityManager): Form {
            val form = Form(
                title = DEFAULT_TITLE,
                description = DEFAULT_DESCRIPTION,
                activated = DEFAULT_ACTIVATED,
                orderNo = DEFAULT_ORDER_NO
            )

            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            form.user = user

            val category = CategoryResourceIT.createEntity(em)
            em.persist(category)
            em.flush()
            form.category = category

            return form
        }

        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Form {
            val form = Form(
                title = UPDATED_TITLE,
                description = UPDATED_DESCRIPTION,
                activated = UPDATED_ACTIVATED,
                orderNo = UPDATED_ORDER_NO
            )

            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            form.user = user

            val category = CategoryResourceIT.createEntity(em)
            em.persist(category)
            em.flush()
            form.category = category

            return form
        }
    }
}
