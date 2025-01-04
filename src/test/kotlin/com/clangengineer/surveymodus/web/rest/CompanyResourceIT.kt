package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.domain.Company
import com.clangengineer.surveymodus.domain.Form
import com.clangengineer.surveymodus.domain.User
import com.clangengineer.surveymodus.domain.embeddable.Staff
import com.clangengineer.surveymodus.domain.enumeration.level
import com.clangengineer.surveymodus.repository.CompanyRepository
import com.clangengineer.surveymodus.service.mapper.CompanyMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.*
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
class CompanyResourceIT {
    @Autowired
    private lateinit var companyRepository: CompanyRepository

    @Autowired
    private lateinit var companyMapper: CompanyMapper

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restCompanyMockMvc: MockMvc

    private lateinit var company: Company

    @BeforeEach
    fun initTest() {
        company = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createCompany() {
        val databaseSizeBeforeCreate = companyRepository.findAll().size

        val companyDTO = companyMapper.toDto(company)
        restCompanyMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(companyDTO))
        ).andExpect(status().isCreated)

        val companyList = companyRepository.findAll()
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1)
        val testCompany = companyList[companyList.size - 1]

        assertThat(testCompany.title).isEqualTo(DEFAULT_TITLE)
        assertThat(testCompany.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testCompany.activated).isEqualTo(DEFAULT_ACTIVATED)
        assertThat(testCompany.user).isEqualTo(company.user)
        assertThat(testCompany.forms).isEqualTo(company.forms)
        assertThat(testCompany.staffs).isEqualTo(company.staffs)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createCompanyWithExistingId() {
        company.id = 1L
        val companyDTO = companyMapper.toDto(company)

        val databaseSizeBeforeCreate = companyRepository.findAll().size

        restCompanyMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(companyDTO))
        ).andExpect(status().isBadRequest)

        val companyList = companyRepository.findAll()
        assertThat(companyList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkTitleIsRequired() {
        val databaseSizeBeforeTest = companyRepository.findAll().size

        company.title = null

        val companyDTO = companyMapper.toDto(company)

        restCompanyMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(companyDTO))
        ).andExpect(status().isBadRequest)

        val companyList = companyRepository.findAll()
        assertThat(companyList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanys() {
        companyRepository.saveAndFlush(company)

        val expectedFormIds = company.forms?.map { it.id?.toInt() }.toTypedArray()
        val expectedStaffEmails = company.staffs?.map { it.email }.toTypedArray()

        restCompanyMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.id?.toInt())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED)))
            .andExpect(jsonPath("$.[*].forms[*].id").value(containsInAnyOrder(*expectedFormIds)))
            .andExpect(jsonPath("$.[*].staffs[*].email").value(containsInAnyOrder(*expectedStaffEmails)))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getCompany() {
        companyRepository.saveAndFlush(company)

        val id = company.id
        assertNotNull(id)

        val expectedFormIds = company.forms?.map { it.id?.toInt() }.toTypedArray()
        val expectedStaffEmails = company.staffs?.map { it.email }.toTypedArray()

        restCompanyMockMvc.perform(get(ENTITY_API_URL_ID, company.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(company.id?.toInt()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED))
            .andExpect(jsonPath("$.forms[*].id").value(containsInAnyOrder(*expectedFormIds)))
            .andExpect(jsonPath("$.staffs[*].email").value(containsInAnyOrder(*expectedStaffEmails)))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getCompanysByIdFiltering() {
        companyRepository.saveAndFlush(company)
        val id = company.id

        defaultCompanyShouldBeFound("id.equals=$id")
        defaultCompanyShouldNotBeFound("id.notEquals=$id")

        defaultCompanyShouldBeFound("id.greaterThanOrEqual=$id")
        defaultCompanyShouldNotBeFound("id.greaterThan=$id")

        defaultCompanyShouldBeFound("id.lessThanOrEqual=$id")
        defaultCompanyShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanysByTitleIsEqualToSomething() {
        companyRepository.saveAndFlush(company)

        defaultCompanyShouldBeFound("title.equals=$DEFAULT_TITLE")

        defaultCompanyShouldNotBeFound("title.equals=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanysByTitleIsInShouldWork() {
        companyRepository.saveAndFlush(company)

        defaultCompanyShouldBeFound("title.in=$DEFAULT_TITLE,$UPDATED_TITLE")

        defaultCompanyShouldNotBeFound("title.in=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanysByTitleIsNullOrNotNull() {
        companyRepository.saveAndFlush(company)

        defaultCompanyShouldBeFound("title.specified=true")

        defaultCompanyShouldNotBeFound("title.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanysByTitleContainsSomething() {
        companyRepository.saveAndFlush(company)

        defaultCompanyShouldBeFound("title.contains=$DEFAULT_TITLE")

        defaultCompanyShouldNotBeFound("title.contains=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanysByTitleNotContainsSomething() {
        companyRepository.saveAndFlush(company)

        defaultCompanyShouldNotBeFound("title.doesNotContain=$DEFAULT_TITLE")

        defaultCompanyShouldBeFound("title.doesNotContain=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanysByDescriptionIsEqualToSomething() {
        companyRepository.saveAndFlush(company)

        defaultCompanyShouldBeFound("description.equals=$DEFAULT_DESCRIPTION")

        defaultCompanyShouldNotBeFound("description.equals=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanysByDescriptionIsInShouldWork() {
        companyRepository.saveAndFlush(company)

        defaultCompanyShouldBeFound("description.in=$DEFAULT_DESCRIPTION,$UPDATED_DESCRIPTION")

        defaultCompanyShouldNotBeFound("description.in=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanysByDescriptionIsNullOrNotNull() {
        companyRepository.saveAndFlush(company)

        defaultCompanyShouldBeFound("description.specified=true")

        defaultCompanyShouldNotBeFound("description.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanysByDescriptionContainsSomething() {
        companyRepository.saveAndFlush(company)

        defaultCompanyShouldBeFound("description.contains=$DEFAULT_DESCRIPTION")

        defaultCompanyShouldNotBeFound("description.contains=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanysByDescriptionNotContainsSomething() {
        companyRepository.saveAndFlush(company)

        defaultCompanyShouldNotBeFound("description.doesNotContain=$DEFAULT_DESCRIPTION")

        defaultCompanyShouldBeFound("description.doesNotContain=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanysByActivatedIsEqualToSomething() {
        companyRepository.saveAndFlush(company)

        defaultCompanyShouldBeFound("activated.equals=$DEFAULT_ACTIVATED")

        defaultCompanyShouldNotBeFound("activated.equals=$UPDATED_ACTIVATED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanysByActivatedIsInShouldWork() {
        companyRepository.saveAndFlush(company)

        defaultCompanyShouldBeFound("activated.in=$DEFAULT_ACTIVATED,$UPDATED_ACTIVATED")

        defaultCompanyShouldNotBeFound("activated.in=$UPDATED_ACTIVATED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanysByActivatedIsNullOrNotNull() {
        companyRepository.saveAndFlush(company)

        defaultCompanyShouldBeFound("activated.specified=true")

        defaultCompanyShouldNotBeFound("activated.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCompanysByUserIsEqualToSomething() {
        var user: User
        if (findAll(em, User::class).isEmpty()) {
            companyRepository.saveAndFlush(company)
            user = UserResourceIT.createEntity(em)
        } else {
            user = findAll(em, User::class)[0]
        }
        em.persist(user)
        em.flush()
        company.user = user
        companyRepository.saveAndFlush(company)
        val userId = user?.id

        defaultCompanyShouldBeFound("userId.equals=$userId")
        defaultCompanyShouldNotBeFound("userId.equals=${(userId?.plus(1))}")
    }

    @Throws(Exception::class)
    private fun defaultCompanyShouldBeFound(filter: String) {
        restCompanyMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.id?.toInt())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED)))
            .andExpect(jsonPath("$.[*].forms[*].id").value(hasItem(company.forms.first().id?.toInt())))
            .andExpect(jsonPath("$.[*].staffs[*].email").value(hasItem(company.staffs.first().email)))

        restCompanyMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    @Throws(Exception::class)
    private fun defaultCompanyShouldNotBeFound(filter: String) {
        restCompanyMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        restCompanyMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingCompany() {
        restCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun putExistingCompany() {
        companyRepository.saveAndFlush(company)

        val databaseSizeBeforeUpdate = companyRepository.findAll().size

        val updatedCompany = companyRepository.findById(company.id).get()
        em.detach(updatedCompany)
        updatedCompany.title = UPDATED_TITLE
        updatedCompany.description = UPDATED_DESCRIPTION
        updatedCompany.activated = UPDATED_ACTIVATED
        val companyDTO = companyMapper.toDto(updatedCompany)

        restCompanyMockMvc.perform(
            put(ENTITY_API_URL_ID, companyDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(companyDTO))
        ).andExpect(status().isOk)

        val companyList = companyRepository.findAll()
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate)
        val testCompany = companyList[companyList.size - 1]
        assertThat(testCompany.title).isEqualTo(UPDATED_TITLE)
        assertThat(testCompany.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testCompany.activated).isEqualTo(UPDATED_ACTIVATED)
    }

    @Test
    @Transactional
    fun putNonExistingCompany() {
        val databaseSizeBeforeUpdate = companyRepository.findAll().size
        company.id = count.incrementAndGet()

        val companyDTO = companyMapper.toDto(company)

        restCompanyMockMvc.perform(
            put(ENTITY_API_URL_ID, companyDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(companyDTO))
        )
            .andExpect(status().isBadRequest)

        val companyList = companyRepository.findAll()
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchCompany() {
        val databaseSizeBeforeUpdate = companyRepository.findAll().size
        company.id = count.incrementAndGet()

        val companyDTO = companyMapper.toDto(company)

        restCompanyMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(companyDTO))
        )
            .andExpect(status().isBadRequest)

        val companyList = companyRepository.findAll()
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamCompany() {
        val databaseSizeBeforeUpdate = companyRepository.findAll().size
        company.id = count.incrementAndGet()

        val companyDTO = companyMapper.toDto(company)

        restCompanyMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(companyDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        val companyList = companyRepository.findAll()
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateCompanyWithPatch() {
        companyRepository.saveAndFlush(company)

        val databaseSizeBeforeUpdate = companyRepository.findAll().size

        val partialUpdatedCompany = Company().apply {
            id = company.id
            title = UPDATED_TITLE
            description = UPDATED_DESCRIPTION
            activated = UPDATED_ACTIVATED
        }

        restCompanyMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedCompany.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedCompany))
        )
            .andExpect(status().isOk)

        val companyList = companyRepository.findAll()
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate)
        val testCompany = companyList.last()
        assertThat(testCompany.title).isEqualTo(UPDATED_TITLE)
        assertThat(testCompany.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testCompany.activated).isEqualTo(UPDATED_ACTIVATED)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateCompanyWithPatch() {
        companyRepository.saveAndFlush(company)

        val databaseSizeBeforeUpdate = companyRepository.findAll().size

        val partialUpdatedCompany = Company().apply {
            id = company.id
            title = UPDATED_TITLE
            description = UPDATED_DESCRIPTION
            activated = UPDATED_ACTIVATED
        }

        restCompanyMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedCompany.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedCompany))
        )
            .andExpect(status().isOk)

        val companyList = companyRepository.findAll()
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate)
        val testCompany = companyList.last()
        assertThat(testCompany.title).isEqualTo(UPDATED_TITLE)
        assertThat(testCompany.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testCompany.activated).isEqualTo(UPDATED_ACTIVATED)
    }

    @Throws(Exception::class)
    fun patchNonExistingCompany() {
        val databaseSizeBeforeUpdate = companyRepository.findAll().size
        company.id = count.incrementAndGet()

        val companyDTO = companyMapper.toDto(company)

        restCompanyMockMvc.perform(
            patch(ENTITY_API_URL_ID, companyDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(companyDTO))
        )
            .andExpect(status().isBadRequest)

        val companyList = companyRepository.findAll()
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchCompany() {
        val databaseSizeBeforeUpdate = companyRepository.findAll().size
        company.id = count.incrementAndGet()

        val companyDTO = companyMapper.toDto(company)

        restCompanyMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(companyDTO))
        )
            .andExpect(status().isBadRequest)

        val companyList = companyRepository.findAll()
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamCompany() {
        val databaseSizeBeforeUpdate = companyRepository.findAll().size
        company.id = count.incrementAndGet()

        val companyDTO = companyMapper.toDto(company)

        restCompanyMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(companyDTO))
        ).andExpect(status().isMethodNotAllowed)

        val companyList = companyRepository.findAll()
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteCompany() {
        companyRepository.saveAndFlush(company)
        val databaseSizeBeforeDelete = companyRepository.findAll().size

        restCompanyMockMvc.perform(
            delete(ENTITY_API_URL_ID, company.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        val companyList = companyRepository.findAll()
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_TITLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        private const val UPDATED_TITLE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB"

        private const val DEFAULT_DESCRIPTION = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        private const val DEFAULT_ACTIVATED: Boolean = false
        private const val UPDATED_ACTIVATED: Boolean = true

        private val DEFAULT_TYPE: level = level.EASY
        private val UPDATED_TYPE: level = level.NORMAL

        private val ENTITY_API_URL: String = "/api/companys"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

        @JvmStatic
        fun createEntity(em: EntityManager): Company {
            val company = Company(
                title = DEFAULT_TITLE,
                description = DEFAULT_DESCRIPTION,
                activated = DEFAULT_ACTIVATED,
            )

            val user = UserResourceIT.createEntity(em)
            em.persist(user)

            val forms = getNewForms(em)
            em.flush()

            company.user = user
            company.forms = forms
            company.staffs = getNewStaffs()

            return company
        }

        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Company {
            val company = Company(
                title = UPDATED_TITLE,
                description = UPDATED_DESCRIPTION,
                activated = UPDATED_ACTIVATED,
            )

            val user = UserResourceIT.createEntity(em)
            em.persist(user)

            val forms = getNewForms(em)
            em.flush()

            company.user = user
            company.forms = forms
            company.staffs = getNewStaffs()

            return company
        }

        fun getNewForms(em: EntityManager): MutableSet<Form> {
            val form1 = FormResourceIT.createEntity(em)
            val form2 = FormResourceIT.createEntity(em)

            em.persist(form1)
            em.persist(form2)
            return mutableSetOf(form1, form2)
        }

        fun getNewStaffs(): MutableSet<Staff> {
            var set = mutableSetOf<Staff>()

            for (i in 1..3) {
                val randomNum = random.nextInt(10) + 1
                set.add(
                    Staff(
                        firstName = "firstName$randomNum", lastName = "lastName$randomNum",
                        email = "email$randomNum@test.com", activated = true,
                        langKey = "en", phone = "010-$randomNum"
                    )
                )
            }

            return set
        }
    }
}
