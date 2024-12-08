package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.IntegrationTest
import com.clangengineer.exformmaker.domain.Company
import com.clangengineer.exformmaker.domain.CompanyForm
import com.clangengineer.exformmaker.domain.Form
import com.clangengineer.exformmaker.repository.CompanyFormRepository
import com.clangengineer.exformmaker.service.CompanyFormService
import com.clangengineer.exformmaker.service.mapper.CompanyFormMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
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
@Extensions(
    ExtendWith(MockitoExtension::class)
)
@AutoConfigureMockMvc
@WithMockUser
class CompanyFormResourceIT {
    @Autowired
    private lateinit var companyFormRepository: CompanyFormRepository

    @Mock
    private lateinit var companyFormRepositoryMock: CompanyFormRepository

    @Autowired
    private lateinit var companyFormMapper: CompanyFormMapper

    @Mock
    private lateinit var companyFormServiceMock: CompanyFormService

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restCompanyFormMockMvc: MockMvc

    private lateinit var companyForm: CompanyForm

    @BeforeEach
    fun initTest() {
        companyForm = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createCompanyForm() {
        val databaseSizeBeforeCreate = companyFormRepository.findAll().size

        val companyFormDTO = companyFormMapper.toDto(companyForm)
        restCompanyFormMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(companyFormDTO))
        ).andExpect(status().isCreated)

        val companyFormList = companyFormRepository.findAll()
        assertThat(companyFormList).hasSize(databaseSizeBeforeCreate + 1)
        val testCompanyForm = companyFormList[companyFormList.size - 1]
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createCompanyFormWithExistingId() {
        companyForm.id = 1L
        val companyFormDTO = companyFormMapper.toDto(companyForm)

        val databaseSizeBeforeCreate = companyFormRepository.findAll().size

        restCompanyFormMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(companyFormDTO))
        ).andExpect(status().isBadRequest)

        val companyFormList = companyFormRepository.findAll()
        assertThat(companyFormList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanyForms() {
        companyFormRepository.saveAndFlush(companyForm)

        restCompanyFormMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyForm.id?.toInt())))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllCompanyFormsWithEagerRelationshipsIsEnabled() {
        `when`(companyFormServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restCompanyFormMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false"))
            .andExpect(status().isOk)

        verify(companyFormRepositoryMock, times(1)).findAll(any(Pageable::class.java))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllCompanyFormsWithEagerRelationshipsIsNotEnabled() {
        `when`(companyFormServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restCompanyFormMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true"))
            .andExpect(status().isOk)

        verify(companyFormServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getCompanyForm() {
        companyFormRepository.saveAndFlush(companyForm)

        val id = companyForm.id
        assertNotNull(id)

        restCompanyFormMockMvc.perform(get(ENTITY_API_URL_ID, companyForm.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(companyForm.id?.toInt()))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getCompanyFormsByIdFiltering() {
        companyFormRepository.saveAndFlush(companyForm)
        val id = companyForm.id

        defaultCompanyFormShouldBeFound("id.equals=$id")
        defaultCompanyFormShouldNotBeFound("id.notEquals=$id")
        defaultCompanyFormShouldBeFound("id.greaterThanOrEqual=$id")
        defaultCompanyFormShouldNotBeFound("id.greaterThan=$id")

        defaultCompanyFormShouldBeFound("id.lessThanOrEqual=$id")
        defaultCompanyFormShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanyFormsByFormIsEqualToSomething() {
        var form: Form
        if (findAll(em, Form::class).isEmpty()) {
            companyFormRepository.saveAndFlush(companyForm)
            form = FormResourceIT.createEntity(em)
        } else {
            form = findAll(em, Form::class)[0]
        }
        em.persist(form)
        em.flush()
        companyForm.form = form
        companyFormRepository.saveAndFlush(companyForm)
        val formId = form?.id

        defaultCompanyFormShouldBeFound("formId.equals=$formId")

        defaultCompanyFormShouldNotBeFound("formId.equals=${(formId?.plus(1))}")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanyFormsByCompanyIsEqualToSomething() {
        var company: Company
        if (findAll(em, Company::class).isEmpty()) {
            companyFormRepository.saveAndFlush(companyForm)
            company = CompanyResourceIT.createEntity(em)
        } else {
            company = findAll(em, Company::class)[0]
        }
        em.persist(company)
        em.flush()
        companyForm.company = company
        companyFormRepository.saveAndFlush(companyForm)
        val companyId = company?.id

        defaultCompanyFormShouldBeFound("companyId.equals=$companyId")

        defaultCompanyFormShouldNotBeFound("companyId.equals=${(companyId?.plus(1))}")
    }

    @Throws(Exception::class)
    private fun defaultCompanyFormShouldBeFound(filter: String) {
        restCompanyFormMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyForm.id?.toInt())))

        restCompanyFormMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    @Throws(Exception::class)
    private fun defaultCompanyFormShouldNotBeFound(filter: String) {
        restCompanyFormMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        restCompanyFormMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingCompanyForm() {
        restCompanyFormMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun putExistingCompanyForm() {
        companyFormRepository.saveAndFlush(companyForm)

        val databaseSizeBeforeUpdate = companyFormRepository.findAll().size

        val updatedCompanyForm = companyFormRepository.findById(companyForm.id).get()

        em.detach(updatedCompanyForm)
        val companyFormDTO = companyFormMapper.toDto(updatedCompanyForm)

        restCompanyFormMockMvc.perform(
            put(ENTITY_API_URL_ID, companyFormDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(companyFormDTO))
        ).andExpect(status().isOk)

        val companyFormList = companyFormRepository.findAll()
        assertThat(companyFormList).hasSize(databaseSizeBeforeUpdate)
        val testCompanyForm = companyFormList[companyFormList.size - 1]
    }

    @Test
    @Transactional
    fun putNonExistingCompanyForm() {
        val databaseSizeBeforeUpdate = companyFormRepository.findAll().size
        companyForm.id = count.incrementAndGet()

        val companyFormDTO = companyFormMapper.toDto(companyForm)

        restCompanyFormMockMvc.perform(
            put(ENTITY_API_URL_ID, companyFormDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(companyFormDTO))
        )
            .andExpect(status().isBadRequest)

        val companyFormList = companyFormRepository.findAll()
        assertThat(companyFormList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchCompanyForm() {
        val databaseSizeBeforeUpdate = companyFormRepository.findAll().size
        companyForm.id = count.incrementAndGet()

        val companyFormDTO = companyFormMapper.toDto(companyForm)

        restCompanyFormMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(companyFormDTO))
        ).andExpect(status().isBadRequest)

        val companyFormList = companyFormRepository.findAll()
        assertThat(companyFormList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamCompanyForm() {
        val databaseSizeBeforeUpdate = companyFormRepository.findAll().size
        companyForm.id = count.incrementAndGet()

        val companyFormDTO = companyFormMapper.toDto(companyForm)

        restCompanyFormMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(companyFormDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        val companyFormList = companyFormRepository.findAll()
        assertThat(companyFormList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateCompanyFormWithPatch() {
        companyFormRepository.saveAndFlush(companyForm)

        val databaseSizeBeforeUpdate = companyFormRepository.findAll().size

        val partialUpdatedCompanyForm = CompanyForm().apply {
            id = companyForm.id
        }

        restCompanyFormMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedCompanyForm.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedCompanyForm))
        )
            .andExpect(status().isOk)

        val companyFormList = companyFormRepository.findAll()
        assertThat(companyFormList).hasSize(databaseSizeBeforeUpdate)
        val testCompanyForm = companyFormList.last()
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateCompanyFormWithPatch() {
        companyFormRepository.saveAndFlush(companyForm)

        val databaseSizeBeforeUpdate = companyFormRepository.findAll().size

        val partialUpdatedCompanyForm = CompanyForm().apply {
            id = companyForm.id
        }

        restCompanyFormMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedCompanyForm.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedCompanyForm))
        )
            .andExpect(status().isOk)

        val companyFormList = companyFormRepository.findAll()
        assertThat(companyFormList).hasSize(databaseSizeBeforeUpdate)
        val testCompanyForm = companyFormList.last()
    }

    @Throws(Exception::class)
    fun patchNonExistingCompanyForm() {
        val databaseSizeBeforeUpdate = companyFormRepository.findAll().size
        companyForm.id = count.incrementAndGet()

        val companyFormDTO = companyFormMapper.toDto(companyForm)

        restCompanyFormMockMvc.perform(
            patch(ENTITY_API_URL_ID, companyFormDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(companyFormDTO))
        ).andExpect(status().isBadRequest)

        val companyFormList = companyFormRepository.findAll()
        assertThat(companyFormList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchCompanyForm() {
        val databaseSizeBeforeUpdate = companyFormRepository.findAll().size
        companyForm.id = count.incrementAndGet()

        val companyFormDTO = companyFormMapper.toDto(companyForm)

        restCompanyFormMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(companyFormDTO))
        )
            .andExpect(status().isBadRequest)

        val companyFormList = companyFormRepository.findAll()
        assertThat(companyFormList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamCompanyForm() {
        val databaseSizeBeforeUpdate = companyFormRepository.findAll().size
        companyForm.id = count.incrementAndGet()

        val companyFormDTO = companyFormMapper.toDto(companyForm)

        restCompanyFormMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(companyFormDTO))
        ).andExpect(status().isMethodNotAllowed)

        val companyFormList = companyFormRepository.findAll()
        assertThat(companyFormList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteCompanyForm() {
        companyFormRepository.saveAndFlush(companyForm)
        val databaseSizeBeforeDelete = companyFormRepository.findAll().size

        restCompanyFormMockMvc.perform(
            delete(ENTITY_API_URL_ID, companyForm.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        val companyFormList = companyFormRepository.findAll()
        assertThat(companyFormList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private val ENTITY_API_URL: String = "/api/company-forms"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

        @JvmStatic
        fun createEntity(em: EntityManager): CompanyForm {
            val companyForm = CompanyForm()

            val form: Form
            if (findAll(em, Form::class).isEmpty()) {
                form = FormResourceIT.createEntity(em)
                em.persist(form)
                em.flush()
            } else {
                form = findAll(em, Form::class)[0]
            }
            companyForm.form = form

            val company: Company
            if (findAll(em, Company::class).isEmpty()) {
                company = CompanyResourceIT.createEntity(em)
                em.persist(company)
                em.flush()
            } else {
                company = findAll(em, Company::class)[0]
            }
            companyForm.company = company
            return companyForm
        }

        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): CompanyForm {
            val companyForm = CompanyForm()

            val form: Form
            if (findAll(em, Form::class).isEmpty()) {
                form = FormResourceIT.createUpdatedEntity(em)
                em.persist(form)
                em.flush()
            } else {
                form = findAll(em, Form::class)[0]
            }
            companyForm.form = form

            val company: Company
            if (findAll(em, Company::class).isEmpty()) {
                company = CompanyResourceIT.createUpdatedEntity(em)
                em.persist(company)
                em.flush()
            } else {
                company = findAll(em, Company::class)[0]
            }
            companyForm.company = company
            return companyForm
        }
    }
}
