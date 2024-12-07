package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.IntegrationTest
import com.clangengineer.exformmaker.domain.Group
import com.clangengineer.exformmaker.domain.User
import com.clangengineer.exformmaker.domain.UserGroup
import com.clangengineer.exformmaker.repository.UserGroupRepository
import com.clangengineer.exformmaker.service.UserGroupService
import com.clangengineer.exformmaker.service.mapper.UserGroupMapper
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
class UserGroupResourceIT {
    @Autowired
    private lateinit var userGroupRepository: UserGroupRepository

    @Mock
    private lateinit var userGroupRepositoryMock: UserGroupRepository

    @Autowired
    private lateinit var userGroupMapper: UserGroupMapper

    @Mock
    private lateinit var userGroupServiceMock: UserGroupService

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restUserGroupMockMvc: MockMvc

    private lateinit var userGroup: UserGroup

    @BeforeEach
    fun initTest() {
        userGroup = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createUserGroup() {
        val databaseSizeBeforeCreate = userGroupRepository.findAll().size

        val userGroupDTO = userGroupMapper.toDto(userGroup)
        restUserGroupMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userGroupDTO))
        ).andExpect(status().isCreated)

        val userGroupList = userGroupRepository.findAll()
        assertThat(userGroupList).hasSize(databaseSizeBeforeCreate + 1)
        val testUserGroup = userGroupList[userGroupList.size - 1]
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createUserGroupWithExistingId() {
        userGroup.id = 1L
        val userGroupDTO = userGroupMapper.toDto(userGroup)

        val databaseSizeBeforeCreate = userGroupRepository.findAll().size

        restUserGroupMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userGroupDTO))
        ).andExpect(status().isBadRequest)

        val userGroupList = userGroupRepository.findAll()
        assertThat(userGroupList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserGroups() {
        userGroupRepository.saveAndFlush(userGroup)

        restUserGroupMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userGroup.id?.toInt())))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllUserGroupsWithEagerRelationshipsIsEnabled() {
        `when`(userGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restUserGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false"))
            .andExpect(status().isOk)

        verify(userGroupRepositoryMock, times(1)).findAll(any(Pageable::class.java))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllUserGroupsWithEagerRelationshipsIsNotEnabled() {
        `when`(userGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restUserGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true"))
            .andExpect(status().isOk)

        verify(userGroupServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getUserGroup() {
        userGroupRepository.saveAndFlush(userGroup)

        val id = userGroup.id
        assertNotNull(id)

        restUserGroupMockMvc.perform(get(ENTITY_API_URL_ID, userGroup.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userGroup.id?.toInt()))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getUserGroupsByIdFiltering() {
        userGroupRepository.saveAndFlush(userGroup)
        val id = userGroup.id

        defaultUserGroupShouldBeFound("id.equals=$id")
        defaultUserGroupShouldNotBeFound("id.notEquals=$id")
        defaultUserGroupShouldBeFound("id.greaterThanOrEqual=$id")
        defaultUserGroupShouldNotBeFound("id.greaterThan=$id")

        defaultUserGroupShouldBeFound("id.lessThanOrEqual=$id")
        defaultUserGroupShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserGroupsByUserIsEqualToSomething() {
        var user: User
        if (findAll(em, User::class).isEmpty()) {
            userGroupRepository.saveAndFlush(userGroup)
            user = UserResourceIT.createEntity(em)
        } else {
            user = findAll(em, User::class)[0]
        }
        em.persist(user)
        em.flush()
        userGroup.user = user
        userGroupRepository.saveAndFlush(userGroup)
        val userId = user?.id

        defaultUserGroupShouldBeFound("userId.equals=$userId")

        defaultUserGroupShouldNotBeFound("userId.equals=${(userId?.plus(1))}")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserGroupsByGroupIsEqualToSomething() {
        var group: Group
        if (findAll(em, Group::class).isEmpty()) {
            userGroupRepository.saveAndFlush(userGroup)
            group = GroupResourceIT.createEntity(em)
        } else {
            group = findAll(em, Group::class)[0]
        }
        em.persist(group)
        em.flush()
        userGroup.group = group
        userGroupRepository.saveAndFlush(userGroup)
        val groupId = group?.id

        defaultUserGroupShouldBeFound("groupId.equals=$groupId")

        defaultUserGroupShouldNotBeFound("groupId.equals=${(groupId?.plus(1))}")
    }

    @Throws(Exception::class)
    private fun defaultUserGroupShouldBeFound(filter: String) {
        restUserGroupMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userGroup.id?.toInt())))

        restUserGroupMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    @Throws(Exception::class)
    private fun defaultUserGroupShouldNotBeFound(filter: String) {
        restUserGroupMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        restUserGroupMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingUserGroup() {
        restUserGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun putExistingUserGroup() {
        userGroupRepository.saveAndFlush(userGroup)

        val databaseSizeBeforeUpdate = userGroupRepository.findAll().size

        val updatedUserGroup = userGroupRepository.findById(userGroup.id).get()

        em.detach(updatedUserGroup)
        val userGroupDTO = userGroupMapper.toDto(updatedUserGroup)

        restUserGroupMockMvc.perform(
            put(ENTITY_API_URL_ID, userGroupDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userGroupDTO))
        ).andExpect(status().isOk)

        val userGroupList = userGroupRepository.findAll()
        assertThat(userGroupList).hasSize(databaseSizeBeforeUpdate)
        val testUserGroup = userGroupList[userGroupList.size - 1]
    }

    @Test
    @Transactional
    fun putNonExistingUserGroup() {
        val databaseSizeBeforeUpdate = userGroupRepository.findAll().size
        userGroup.id = count.incrementAndGet()

        val userGroupDTO = userGroupMapper.toDto(userGroup)

        restUserGroupMockMvc.perform(
            put(ENTITY_API_URL_ID, userGroupDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userGroupDTO))
        )
            .andExpect(status().isBadRequest)

        val userGroupList = userGroupRepository.findAll()
        assertThat(userGroupList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchUserGroup() {
        val databaseSizeBeforeUpdate = userGroupRepository.findAll().size
        userGroup.id = count.incrementAndGet()

        val userGroupDTO = userGroupMapper.toDto(userGroup)

        restUserGroupMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userGroupDTO))
        ).andExpect(status().isBadRequest)

        val userGroupList = userGroupRepository.findAll()
        assertThat(userGroupList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamUserGroup() {
        val databaseSizeBeforeUpdate = userGroupRepository.findAll().size
        userGroup.id = count.incrementAndGet()

        val userGroupDTO = userGroupMapper.toDto(userGroup)

        restUserGroupMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userGroupDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        val userGroupList = userGroupRepository.findAll()
        assertThat(userGroupList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateUserGroupWithPatch() {
        userGroupRepository.saveAndFlush(userGroup)

        val databaseSizeBeforeUpdate = userGroupRepository.findAll().size

        val partialUpdatedUserGroup = UserGroup().apply {
            id = userGroup.id
        }

        restUserGroupMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedUserGroup.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedUserGroup))
        )
            .andExpect(status().isOk)

        val userGroupList = userGroupRepository.findAll()
        assertThat(userGroupList).hasSize(databaseSizeBeforeUpdate)
        val testUserGroup = userGroupList.last()
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateUserGroupWithPatch() {
        userGroupRepository.saveAndFlush(userGroup)

        val databaseSizeBeforeUpdate = userGroupRepository.findAll().size

        val partialUpdatedUserGroup = UserGroup().apply {
            id = userGroup.id
        }

        restUserGroupMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedUserGroup.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedUserGroup))
        )
            .andExpect(status().isOk)

        val userGroupList = userGroupRepository.findAll()
        assertThat(userGroupList).hasSize(databaseSizeBeforeUpdate)
        val testUserGroup = userGroupList.last()
    }

    @Throws(Exception::class)
    fun patchNonExistingUserGroup() {
        val databaseSizeBeforeUpdate = userGroupRepository.findAll().size
        userGroup.id = count.incrementAndGet()

        val userGroupDTO = userGroupMapper.toDto(userGroup)

        restUserGroupMockMvc.perform(
            patch(ENTITY_API_URL_ID, userGroupDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(userGroupDTO))
        ).andExpect(status().isBadRequest)

        val userGroupList = userGroupRepository.findAll()
        assertThat(userGroupList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchUserGroup() {
        val databaseSizeBeforeUpdate = userGroupRepository.findAll().size
        userGroup.id = count.incrementAndGet()

        val userGroupDTO = userGroupMapper.toDto(userGroup)

        restUserGroupMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(userGroupDTO))
        )
            .andExpect(status().isBadRequest)

        val userGroupList = userGroupRepository.findAll()
        assertThat(userGroupList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamUserGroup() {
        val databaseSizeBeforeUpdate = userGroupRepository.findAll().size
        userGroup.id = count.incrementAndGet()

        val userGroupDTO = userGroupMapper.toDto(userGroup)

        restUserGroupMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(userGroupDTO))
        ).andExpect(status().isMethodNotAllowed)

        val userGroupList = userGroupRepository.findAll()
        assertThat(userGroupList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteUserGroup() {
        userGroupRepository.saveAndFlush(userGroup)
        val databaseSizeBeforeDelete = userGroupRepository.findAll().size

        restUserGroupMockMvc.perform(
            delete(ENTITY_API_URL_ID, userGroup.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        val userGroupList = userGroupRepository.findAll()
        assertThat(userGroupList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private val ENTITY_API_URL: String = "/api/user-groups"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

        @JvmStatic
        fun createEntity(em: EntityManager): UserGroup {
            val userGroup = UserGroup()

            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            userGroup.user = user

            val group: Group
            if (findAll(em, Group::class).isEmpty()) {
                group = GroupResourceIT.createEntity(em)
                em.persist(group)
                em.flush()
            } else {
                group = findAll(em, Group::class)[0]
            }
            userGroup.group = group
            return userGroup
        }

        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): UserGroup {
            val userGroup = UserGroup()

            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            userGroup.user = user

            val group: Group
            if (findAll(em, Group::class).isEmpty()) {
                group = GroupResourceIT.createUpdatedEntity(em)
                em.persist(group)
                em.flush()
            } else {
                group = findAll(em, Group::class)[0]
            }
            userGroup.group = group
            return userGroup
        }
    }
}
