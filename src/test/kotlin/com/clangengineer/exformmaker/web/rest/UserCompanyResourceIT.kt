package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.IntegrationTest
import com.clangengineer.exformmaker.domain.Company
import com.clangengineer.exformmaker.domain.User
import com.clangengineer.exformmaker.domain.UserCompany
import com.clangengineer.exformmaker.repository.UserCompanyRepository
import com.clangengineer.exformmaker.service.UserCompanyService
import com.clangengineer.exformmaker.service.mapper.UserCompanyMapper
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
class UserCompanyResourceIT {
    @Autowired
    private lateinit var userCompanyRepository: UserCompanyRepository

    @Mock
    private lateinit var userCompanyRepositoryMock: UserCompanyRepository

    @Autowired
    private lateinit var userCompanyMapper: UserCompanyMapper

    @Mock
    private lateinit var userCompanyServiceMock: UserCompanyService

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restUserCompanyMockMvc: MockMvc

    private lateinit var userCompany: UserCompany

    @BeforeEach
    fun initTest() {
        userCompany = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createUserCompany() {
        val databaseSizeBeforeCreate = userCompanyRepository.findAll().size

        val userCompanyDTO = userCompanyMapper.toDto(userCompany)
        restUserCompanyMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userCompanyDTO))
        ).andExpect(status().isCreated)

        val userCompanyList = userCompanyRepository.findAll()
        assertThat(userCompanyList).hasSize(databaseSizeBeforeCreate + 1)
        val testUserCompany = userCompanyList[userCompanyList.size - 1]
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createUserCompanyWithExistingId() {
        userCompany.id = 1L
        val userCompanyDTO = userCompanyMapper.toDto(userCompany)

        val databaseSizeBeforeCreate = userCompanyRepository.findAll().size

        restUserCompanyMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userCompanyDTO))
        ).andExpect(status().isBadRequest)

        val userCompanyList = userCompanyRepository.findAll()
        assertThat(userCompanyList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserCompanys() {
        userCompanyRepository.saveAndFlush(userCompany)

        restUserCompanyMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userCompany.id?.toInt())))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllUserCompanysWithEagerRelationshipsIsEnabled() {
        `when`(userCompanyServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restUserCompanyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false"))
            .andExpect(status().isOk)

        verify(userCompanyRepositoryMock, times(1)).findAll(any(Pageable::class.java))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllUserCompanysWithEagerRelationshipsIsNotEnabled() {
        `when`(userCompanyServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restUserCompanyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true"))
            .andExpect(status().isOk)

        verify(userCompanyServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getUserCompany() {
        userCompanyRepository.saveAndFlush(userCompany)

        val id = userCompany.id
        assertNotNull(id)

        restUserCompanyMockMvc.perform(get(ENTITY_API_URL_ID, userCompany.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userCompany.id?.toInt()))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getUserCompanysByIdFiltering() {
        userCompanyRepository.saveAndFlush(userCompany)
        val id = userCompany.id

        defaultUserCompanyShouldBeFound("id.equals=$id")
        defaultUserCompanyShouldNotBeFound("id.notEquals=$id")
        defaultUserCompanyShouldBeFound("id.greaterThanOrEqual=$id")
        defaultUserCompanyShouldNotBeFound("id.greaterThan=$id")

        defaultUserCompanyShouldBeFound("id.lessThanOrEqual=$id")
        defaultUserCompanyShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserCompanysByUserIsEqualToSomething() {
        var user: User
        if (findAll(em, User::class).isEmpty()) {
            userCompanyRepository.saveAndFlush(userCompany)
            user = UserResourceIT.createEntity(em)
        } else {
            user = findAll(em, User::class)[0]
        }
        em.persist(user)
        em.flush()
        userCompany.user = user
        userCompanyRepository.saveAndFlush(userCompany)
        val userId = user?.id

        defaultUserCompanyShouldBeFound("userId.equals=$userId")

        defaultUserCompanyShouldNotBeFound("userId.equals=${(userId?.plus(1))}")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserCompanysByCompanyIsEqualToSomething() {
        var company: Company
        if (findAll(em, Company::class).isEmpty()) {
            userCompanyRepository.saveAndFlush(userCompany)
            company = CompanyResourceIT.createEntity(em)
        } else {
            company = findAll(em, Company::class)[0]
        }
        em.persist(company)
        em.flush()
        userCompany.company = company
        userCompanyRepository.saveAndFlush(userCompany)
        val companyId = company?.id

        defaultUserCompanyShouldBeFound("companyId.equals=$companyId")

        defaultUserCompanyShouldNotBeFound("companyId.equals=${(companyId?.plus(1))}")
    }

    @Throws(Exception::class)
    private fun defaultUserCompanyShouldBeFound(filter: String) {
        restUserCompanyMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userCompany.id?.toInt())))

        restUserCompanyMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    @Throws(Exception::class)
    private fun defaultUserCompanyShouldNotBeFound(filter: String) {
        restUserCompanyMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        restUserCompanyMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingUserCompany() {
        restUserCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun putExistingUserCompany() {
        userCompanyRepository.saveAndFlush(userCompany)

        val databaseSizeBeforeUpdate = userCompanyRepository.findAll().size

        val updatedUserCompany = userCompanyRepository.findById(userCompany.id).get()

        em.detach(updatedUserCompany)
        val userCompanyDTO = userCompanyMapper.toDto(updatedUserCompany)

        restUserCompanyMockMvc.perform(
            put(ENTITY_API_URL_ID, userCompanyDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userCompanyDTO))
        ).andExpect(status().isOk)

        val userCompanyList = userCompanyRepository.findAll()
        assertThat(userCompanyList).hasSize(databaseSizeBeforeUpdate)
        val testUserCompany = userCompanyList[userCompanyList.size - 1]
    }

    @Test
    @Transactional
    fun putNonExistingUserCompany() {
        val databaseSizeBeforeUpdate = userCompanyRepository.findAll().size
        userCompany.id = count.incrementAndGet()

        val userCompanyDTO = userCompanyMapper.toDto(userCompany)

        restUserCompanyMockMvc.perform(
            put(ENTITY_API_URL_ID, userCompanyDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userCompanyDTO))
        )
            .andExpect(status().isBadRequest)

        val userCompanyList = userCompanyRepository.findAll()
        assertThat(userCompanyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchUserCompany() {
        val databaseSizeBeforeUpdate = userCompanyRepository.findAll().size
        userCompany.id = count.incrementAndGet()

        val userCompanyDTO = userCompanyMapper.toDto(userCompany)

        restUserCompanyMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userCompanyDTO))
        ).andExpect(status().isBadRequest)

        val userCompanyList = userCompanyRepository.findAll()
        assertThat(userCompanyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamUserCompany() {
        val databaseSizeBeforeUpdate = userCompanyRepository.findAll().size
        userCompany.id = count.incrementAndGet()

        val userCompanyDTO = userCompanyMapper.toDto(userCompany)

        restUserCompanyMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userCompanyDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        val userCompanyList = userCompanyRepository.findAll()
        assertThat(userCompanyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateUserCompanyWithPatch() {
        userCompanyRepository.saveAndFlush(userCompany)

        val databaseSizeBeforeUpdate = userCompanyRepository.findAll().size

        val partialUpdatedUserCompany = UserCompany().apply {
            id = userCompany.id
        }

        restUserCompanyMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedUserCompany.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedUserCompany))
        )
            .andExpect(status().isOk)

        val userCompanyList = userCompanyRepository.findAll()
        assertThat(userCompanyList).hasSize(databaseSizeBeforeUpdate)
        val testUserCompany = userCompanyList.last()
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateUserCompanyWithPatch() {
        userCompanyRepository.saveAndFlush(userCompany)

        val databaseSizeBeforeUpdate = userCompanyRepository.findAll().size

        val partialUpdatedUserCompany = UserCompany().apply {
            id = userCompany.id
        }

        restUserCompanyMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedUserCompany.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedUserCompany))
        )
            .andExpect(status().isOk)

        val userCompanyList = userCompanyRepository.findAll()
        assertThat(userCompanyList).hasSize(databaseSizeBeforeUpdate)
        val testUserCompany = userCompanyList.last()
    }

    @Throws(Exception::class)
    fun patchNonExistingUserCompany() {
        val databaseSizeBeforeUpdate = userCompanyRepository.findAll().size
        userCompany.id = count.incrementAndGet()

        val userCompanyDTO = userCompanyMapper.toDto(userCompany)

        restUserCompanyMockMvc.perform(
            patch(ENTITY_API_URL_ID, userCompanyDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(userCompanyDTO))
        ).andExpect(status().isBadRequest)

        val userCompanyList = userCompanyRepository.findAll()
        assertThat(userCompanyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchUserCompany() {
        val databaseSizeBeforeUpdate = userCompanyRepository.findAll().size
        userCompany.id = count.incrementAndGet()

        val userCompanyDTO = userCompanyMapper.toDto(userCompany)

        restUserCompanyMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(userCompanyDTO))
        )
            .andExpect(status().isBadRequest)

        val userCompanyList = userCompanyRepository.findAll()
        assertThat(userCompanyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamUserCompany() {
        val databaseSizeBeforeUpdate = userCompanyRepository.findAll().size
        userCompany.id = count.incrementAndGet()

        val userCompanyDTO = userCompanyMapper.toDto(userCompany)

        restUserCompanyMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(userCompanyDTO))
        ).andExpect(status().isMethodNotAllowed)

        val userCompanyList = userCompanyRepository.findAll()
        assertThat(userCompanyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteUserCompany() {
        userCompanyRepository.saveAndFlush(userCompany)
        val databaseSizeBeforeDelete = userCompanyRepository.findAll().size

        restUserCompanyMockMvc.perform(
            delete(ENTITY_API_URL_ID, userCompany.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        val userCompanyList = userCompanyRepository.findAll()
        assertThat(userCompanyList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private val ENTITY_API_URL: String = "/api/user-companys"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

        @JvmStatic
        fun createEntity(em: EntityManager): UserCompany {
            val userCompany = UserCompany()

            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            userCompany.user = user

            val company: Company
            if (findAll(em, Company::class).isEmpty()) {
                company = CompanyResourceIT.createEntity(em)
                em.persist(company)
                em.flush()
            } else {
                company = findAll(em, Company::class)[0]
            }
            userCompany.company = company
            return userCompany
        }

        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): UserCompany {
            val userCompany = UserCompany()

            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            userCompany.user = user

            val company: Company
            if (findAll(em, Company::class).isEmpty()) {
                company = CompanyResourceIT.createUpdatedEntity(em)
                em.persist(company)
                em.flush()
            } else {
                company = findAll(em, Company::class)[0]
            }
            userCompany.company = company
            return userCompany
        }
    }
}
