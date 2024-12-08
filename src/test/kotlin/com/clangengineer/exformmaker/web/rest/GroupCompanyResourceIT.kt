package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.IntegrationTest
import com.clangengineer.exformmaker.domain.Company
import com.clangengineer.exformmaker.domain.Group
import com.clangengineer.exformmaker.domain.GroupCompany
import com.clangengineer.exformmaker.repository.GroupCompanyRepository
import com.clangengineer.exformmaker.service.GroupCompanyService
import com.clangengineer.exformmaker.service.mapper.GroupCompanyMapper
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
class GroupCompanyResourceIT {
    @Autowired
    private lateinit var groupCompanyRepository: GroupCompanyRepository

    @Mock
    private lateinit var groupCompanyRepositoryMock: GroupCompanyRepository

    @Autowired
    private lateinit var groupCompanyMapper: GroupCompanyMapper

    @Mock
    private lateinit var groupCompanyServiceMock: GroupCompanyService

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restGroupCompanyMockMvc: MockMvc

    private lateinit var groupCompany: GroupCompany

    @BeforeEach
    fun initTest() {
        groupCompany = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createGroupCompany() {
        val databaseSizeBeforeCreate = groupCompanyRepository.findAll().size

        val groupCompanyDTO = groupCompanyMapper.toDto(groupCompany)
        restGroupCompanyMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupCompanyDTO))
        ).andExpect(status().isCreated)

        val groupCompanyList = groupCompanyRepository.findAll()
        assertThat(groupCompanyList).hasSize(databaseSizeBeforeCreate + 1)
        val testGroupCompany = groupCompanyList[groupCompanyList.size - 1]
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createGroupCompanyWithExistingId() {
        groupCompany.id = 1L
        val groupCompanyDTO = groupCompanyMapper.toDto(groupCompany)

        val databaseSizeBeforeCreate = groupCompanyRepository.findAll().size

        restGroupCompanyMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupCompanyDTO))
        ).andExpect(status().isBadRequest)

        val groupCompanyList = groupCompanyRepository.findAll()
        assertThat(groupCompanyList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupCompanys() {
        groupCompanyRepository.saveAndFlush(groupCompany)

        restGroupCompanyMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupCompany.id?.toInt())))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllGroupCompanysWithEagerRelationshipsIsEnabled() {
        `when`(groupCompanyServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restGroupCompanyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false"))
            .andExpect(status().isOk)

        verify(groupCompanyRepositoryMock, times(1)).findAll(any(Pageable::class.java))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllGroupCompanysWithEagerRelationshipsIsNotEnabled() {
        `when`(groupCompanyServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restGroupCompanyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true"))
            .andExpect(status().isOk)

        verify(groupCompanyServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getGroupCompany() {
        groupCompanyRepository.saveAndFlush(groupCompany)

        val id = groupCompany.id
        assertNotNull(id)

        restGroupCompanyMockMvc.perform(get(ENTITY_API_URL_ID, groupCompany.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(groupCompany.id?.toInt()))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getGroupCompanysByIdFiltering() {
        groupCompanyRepository.saveAndFlush(groupCompany)
        val id = groupCompany.id

        defaultGroupCompanyShouldBeFound("id.equals=$id")
        defaultGroupCompanyShouldNotBeFound("id.notEquals=$id")
        defaultGroupCompanyShouldBeFound("id.greaterThanOrEqual=$id")
        defaultGroupCompanyShouldNotBeFound("id.greaterThan=$id")

        defaultGroupCompanyShouldBeFound("id.lessThanOrEqual=$id")
        defaultGroupCompanyShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupCompanysByGroupIsEqualToSomething() {
        var group: Group
        if (findAll(em, Group::class).isEmpty()) {
            groupCompanyRepository.saveAndFlush(groupCompany)
            group = GroupResourceIT.createEntity(em)
        } else {
            group = findAll(em, Group::class)[0]
        }
        em.persist(group)
        em.flush()
        groupCompany.group = group
        groupCompanyRepository.saveAndFlush(groupCompany)
        val groupId = group?.id

        defaultGroupCompanyShouldBeFound("groupId.equals=$groupId")

        defaultGroupCompanyShouldNotBeFound("groupId.equals=${(groupId?.plus(1))}")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupCompanysByCompanyIsEqualToSomething() {
        var company: Company
        if (findAll(em, Company::class).isEmpty()) {
            groupCompanyRepository.saveAndFlush(groupCompany)
            company = CompanyResourceIT.createEntity(em)
        } else {
            company = findAll(em, Company::class)[0]
        }
        em.persist(company)
        em.flush()
        groupCompany.company = company
        groupCompanyRepository.saveAndFlush(groupCompany)
        val companyId = company?.id

        defaultGroupCompanyShouldBeFound("companyId.equals=$companyId")

        defaultGroupCompanyShouldNotBeFound("companyId.equals=${(companyId?.plus(1))}")
    }

    @Throws(Exception::class)
    private fun defaultGroupCompanyShouldBeFound(filter: String) {
        restGroupCompanyMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupCompany.id?.toInt())))

        restGroupCompanyMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    @Throws(Exception::class)
    private fun defaultGroupCompanyShouldNotBeFound(filter: String) {
        restGroupCompanyMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        restGroupCompanyMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingGroupCompany() {
        restGroupCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun putExistingGroupCompany() {
        groupCompanyRepository.saveAndFlush(groupCompany)

        val databaseSizeBeforeUpdate = groupCompanyRepository.findAll().size

        val updatedGroupCompany = groupCompanyRepository.findById(groupCompany.id).get()

        em.detach(updatedGroupCompany)
        val groupCompanyDTO = groupCompanyMapper.toDto(updatedGroupCompany)

        restGroupCompanyMockMvc.perform(
            put(ENTITY_API_URL_ID, groupCompanyDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupCompanyDTO))
        ).andExpect(status().isOk)

        val groupCompanyList = groupCompanyRepository.findAll()
        assertThat(groupCompanyList).hasSize(databaseSizeBeforeUpdate)
        val testGroupCompany = groupCompanyList[groupCompanyList.size - 1]
    }

    @Test
    @Transactional
    fun putNonExistingGroupCompany() {
        val databaseSizeBeforeUpdate = groupCompanyRepository.findAll().size
        groupCompany.id = count.incrementAndGet()

        val groupCompanyDTO = groupCompanyMapper.toDto(groupCompany)

        restGroupCompanyMockMvc.perform(
            put(ENTITY_API_URL_ID, groupCompanyDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupCompanyDTO))
        )
            .andExpect(status().isBadRequest)

        val groupCompanyList = groupCompanyRepository.findAll()
        assertThat(groupCompanyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchGroupCompany() {
        val databaseSizeBeforeUpdate = groupCompanyRepository.findAll().size
        groupCompany.id = count.incrementAndGet()

        val groupCompanyDTO = groupCompanyMapper.toDto(groupCompany)

        restGroupCompanyMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupCompanyDTO))
        ).andExpect(status().isBadRequest)

        val groupCompanyList = groupCompanyRepository.findAll()
        assertThat(groupCompanyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamGroupCompany() {
        val databaseSizeBeforeUpdate = groupCompanyRepository.findAll().size
        groupCompany.id = count.incrementAndGet()

        val groupCompanyDTO = groupCompanyMapper.toDto(groupCompany)

        restGroupCompanyMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupCompanyDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        val groupCompanyList = groupCompanyRepository.findAll()
        assertThat(groupCompanyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateGroupCompanyWithPatch() {
        groupCompanyRepository.saveAndFlush(groupCompany)

        val databaseSizeBeforeUpdate = groupCompanyRepository.findAll().size

        val partialUpdatedGroupCompany = GroupCompany().apply {
            id = groupCompany.id
        }

        restGroupCompanyMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedGroupCompany.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedGroupCompany))
        )
            .andExpect(status().isOk)

        val groupCompanyList = groupCompanyRepository.findAll()
        assertThat(groupCompanyList).hasSize(databaseSizeBeforeUpdate)
        val testGroupCompany = groupCompanyList.last()
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateGroupCompanyWithPatch() {
        groupCompanyRepository.saveAndFlush(groupCompany)

        val databaseSizeBeforeUpdate = groupCompanyRepository.findAll().size

        val partialUpdatedGroupCompany = GroupCompany().apply {
            id = groupCompany.id
        }

        restGroupCompanyMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedGroupCompany.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedGroupCompany))
        )
            .andExpect(status().isOk)

        val groupCompanyList = groupCompanyRepository.findAll()
        assertThat(groupCompanyList).hasSize(databaseSizeBeforeUpdate)
        val testGroupCompany = groupCompanyList.last()
    }

    @Throws(Exception::class)
    fun patchNonExistingGroupCompany() {
        val databaseSizeBeforeUpdate = groupCompanyRepository.findAll().size
        groupCompany.id = count.incrementAndGet()

        val groupCompanyDTO = groupCompanyMapper.toDto(groupCompany)

        restGroupCompanyMockMvc.perform(
            patch(ENTITY_API_URL_ID, groupCompanyDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(groupCompanyDTO))
        ).andExpect(status().isBadRequest)

        val groupCompanyList = groupCompanyRepository.findAll()
        assertThat(groupCompanyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchGroupCompany() {
        val databaseSizeBeforeUpdate = groupCompanyRepository.findAll().size
        groupCompany.id = count.incrementAndGet()

        val groupCompanyDTO = groupCompanyMapper.toDto(groupCompany)

        restGroupCompanyMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(groupCompanyDTO))
        )
            .andExpect(status().isBadRequest)

        val groupCompanyList = groupCompanyRepository.findAll()
        assertThat(groupCompanyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamGroupCompany() {
        val databaseSizeBeforeUpdate = groupCompanyRepository.findAll().size
        groupCompany.id = count.incrementAndGet()

        val groupCompanyDTO = groupCompanyMapper.toDto(groupCompany)

        restGroupCompanyMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(groupCompanyDTO))
        ).andExpect(status().isMethodNotAllowed)

        val groupCompanyList = groupCompanyRepository.findAll()
        assertThat(groupCompanyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteGroupCompany() {
        groupCompanyRepository.saveAndFlush(groupCompany)
        val databaseSizeBeforeDelete = groupCompanyRepository.findAll().size

        restGroupCompanyMockMvc.perform(
            delete(ENTITY_API_URL_ID, groupCompany.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        val groupCompanyList = groupCompanyRepository.findAll()
        assertThat(groupCompanyList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private val ENTITY_API_URL: String = "/api/group-companys"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

        @JvmStatic
        fun createEntity(em: EntityManager): GroupCompany {
            val groupCompany = GroupCompany()

            val group: Group
            if (findAll(em, Group::class).isEmpty()) {
                group = GroupResourceIT.createEntity(em)
                em.persist(group)
                em.flush()
            } else {
                group = findAll(em, Group::class)[0]
            }
            groupCompany.group = group

            val company: Company
            if (findAll(em, Company::class).isEmpty()) {
                company = CompanyResourceIT.createEntity(em)
                em.persist(company)
                em.flush()
            } else {
                company = findAll(em, Company::class)[0]
            }
            groupCompany.company = company
            return groupCompany
        }

        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): GroupCompany {
            val groupCompany = GroupCompany()

            val group: Group
            if (findAll(em, Group::class).isEmpty()) {
                group = GroupResourceIT.createEntity(em)
                em.persist(group)
                em.flush()
            } else {
                group = findAll(em, Group::class)[0]
            }
            groupCompany.group = group

            val company: Company
            if (findAll(em, Company::class).isEmpty()) {
                company = CompanyResourceIT.createUpdatedEntity(em)
                em.persist(company)
                em.flush()
            } else {
                company = findAll(em, Company::class)[0]
            }
            groupCompany.company = company
            return groupCompany
        }
    }
}
