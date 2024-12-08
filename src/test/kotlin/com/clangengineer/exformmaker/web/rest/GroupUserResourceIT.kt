package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.IntegrationTest
import com.clangengineer.exformmaker.domain.Group
import com.clangengineer.exformmaker.domain.GroupUser
import com.clangengineer.exformmaker.domain.User
import com.clangengineer.exformmaker.repository.GroupUserRepository
import com.clangengineer.exformmaker.service.GroupUserService
import com.clangengineer.exformmaker.service.mapper.GroupUserMapper
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
class GroupUserResourceIT {
    @Autowired
    private lateinit var groupUserRepository: GroupUserRepository

    @Mock
    private lateinit var groupUserRepositoryMock: GroupUserRepository

    @Autowired
    private lateinit var groupUserMapper: GroupUserMapper

    @Mock
    private lateinit var groupUserServiceMock: GroupUserService

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restGroupUserMockMvc: MockMvc

    private lateinit var groupUser: GroupUser

    @BeforeEach
    fun initTest() {
        groupUser = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createGroupUser() {
        val databaseSizeBeforeCreate = groupUserRepository.findAll().size

        val groupUserDTO = groupUserMapper.toDto(groupUser)
        restGroupUserMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupUserDTO))
        ).andExpect(status().isCreated)

        val groupUserList = groupUserRepository.findAll()
        assertThat(groupUserList).hasSize(databaseSizeBeforeCreate + 1)
        val testGroupUser = groupUserList[groupUserList.size - 1]
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createGroupUserWithExistingId() {
        groupUser.id = 1L
        val groupUserDTO = groupUserMapper.toDto(groupUser)

        val databaseSizeBeforeCreate = groupUserRepository.findAll().size

        restGroupUserMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupUserDTO))
        ).andExpect(status().isBadRequest)

        val groupUserList = groupUserRepository.findAll()
        assertThat(groupUserList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupUsers() {
        groupUserRepository.saveAndFlush(groupUser)

        restGroupUserMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupUser.id?.toInt())))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllGroupUsersWithEagerRelationshipsIsEnabled() {
        `when`(groupUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restGroupUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false"))
            .andExpect(status().isOk)

        verify(groupUserRepositoryMock, times(1)).findAll(any(Pageable::class.java))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllGroupUsersWithEagerRelationshipsIsNotEnabled() {
        `when`(groupUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restGroupUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true"))
            .andExpect(status().isOk)

        verify(groupUserServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getGroupUser() {
        groupUserRepository.saveAndFlush(groupUser)

        val id = groupUser.id
        assertNotNull(id)

        restGroupUserMockMvc.perform(get(ENTITY_API_URL_ID, groupUser.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(groupUser.id?.toInt()))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getGroupUsersByIdFiltering() {
        groupUserRepository.saveAndFlush(groupUser)
        val id = groupUser.id

        defaultGroupUserShouldBeFound("id.equals=$id")
        defaultGroupUserShouldNotBeFound("id.notEquals=$id")
        defaultGroupUserShouldBeFound("id.greaterThanOrEqual=$id")
        defaultGroupUserShouldNotBeFound("id.greaterThan=$id")

        defaultGroupUserShouldBeFound("id.lessThanOrEqual=$id")
        defaultGroupUserShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupUsersByUserIsEqualToSomething() {
        var user: User
        if (findAll(em, User::class).isEmpty()) {
            groupUserRepository.saveAndFlush(groupUser)
            user = UserResourceIT.createEntity(em)
        } else {
            user = findAll(em, User::class)[0]
        }
        em.persist(user)
        em.flush()
        groupUser.user = user
        groupUserRepository.saveAndFlush(groupUser)
        val userId = user?.id

        defaultGroupUserShouldBeFound("userId.equals=$userId")

        defaultGroupUserShouldNotBeFound("userId.equals=${(userId?.plus(1))}")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupUsersByGroupIsEqualToSomething() {
        var group: Group
        if (findAll(em, Group::class).isEmpty()) {
            groupUserRepository.saveAndFlush(groupUser)
            group = GroupResourceIT.createEntity(em)
        } else {
            group = findAll(em, Group::class)[0]
        }
        em.persist(group)
        em.flush()
        groupUser.group = group
        groupUserRepository.saveAndFlush(groupUser)
        val groupId = group?.id

        defaultGroupUserShouldBeFound("groupId.equals=$groupId")

        defaultGroupUserShouldNotBeFound("groupId.equals=${(groupId?.plus(1))}")
    }

    @Throws(Exception::class)
    private fun defaultGroupUserShouldBeFound(filter: String) {
        restGroupUserMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupUser.id?.toInt())))

        restGroupUserMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    @Throws(Exception::class)
    private fun defaultGroupUserShouldNotBeFound(filter: String) {
        restGroupUserMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        restGroupUserMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingGroupUser() {
        restGroupUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun putExistingGroupUser() {
        groupUserRepository.saveAndFlush(groupUser)

        val databaseSizeBeforeUpdate = groupUserRepository.findAll().size

        val updatedGroupUser = groupUserRepository.findById(groupUser.id).get()

        em.detach(updatedGroupUser)
        val groupUserDTO = groupUserMapper.toDto(updatedGroupUser)

        restGroupUserMockMvc.perform(
            put(ENTITY_API_URL_ID, groupUserDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupUserDTO))
        ).andExpect(status().isOk)

        val groupUserList = groupUserRepository.findAll()
        assertThat(groupUserList).hasSize(databaseSizeBeforeUpdate)
        val testGroupUser = groupUserList[groupUserList.size - 1]
    }

    @Test
    @Transactional
    fun putNonExistingGroupUser() {
        val databaseSizeBeforeUpdate = groupUserRepository.findAll().size
        groupUser.id = count.incrementAndGet()

        val groupUserDTO = groupUserMapper.toDto(groupUser)

        restGroupUserMockMvc.perform(
            put(ENTITY_API_URL_ID, groupUserDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupUserDTO))
        )
            .andExpect(status().isBadRequest)

        val groupUserList = groupUserRepository.findAll()
        assertThat(groupUserList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchGroupUser() {
        val databaseSizeBeforeUpdate = groupUserRepository.findAll().size
        groupUser.id = count.incrementAndGet()

        val groupUserDTO = groupUserMapper.toDto(groupUser)

        restGroupUserMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupUserDTO))
        ).andExpect(status().isBadRequest)

        val groupUserList = groupUserRepository.findAll()
        assertThat(groupUserList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamGroupUser() {
        val databaseSizeBeforeUpdate = groupUserRepository.findAll().size
        groupUser.id = count.incrementAndGet()

        val groupUserDTO = groupUserMapper.toDto(groupUser)

        restGroupUserMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupUserDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        val groupUserList = groupUserRepository.findAll()
        assertThat(groupUserList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateGroupUserWithPatch() {
        groupUserRepository.saveAndFlush(groupUser)

        val databaseSizeBeforeUpdate = groupUserRepository.findAll().size

        val partialUpdatedGroupUser = GroupUser().apply {
            id = groupUser.id
        }

        restGroupUserMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedGroupUser.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedGroupUser))
        )
            .andExpect(status().isOk)

        val groupUserList = groupUserRepository.findAll()
        assertThat(groupUserList).hasSize(databaseSizeBeforeUpdate)
        val testGroupUser = groupUserList.last()
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateGroupUserWithPatch() {
        groupUserRepository.saveAndFlush(groupUser)

        val databaseSizeBeforeUpdate = groupUserRepository.findAll().size

        val partialUpdatedGroupUser = GroupUser().apply {
            id = groupUser.id
        }

        restGroupUserMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedGroupUser.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedGroupUser))
        )
            .andExpect(status().isOk)

        val groupUserList = groupUserRepository.findAll()
        assertThat(groupUserList).hasSize(databaseSizeBeforeUpdate)
        val testGroupUser = groupUserList.last()
    }

    @Throws(Exception::class)
    fun patchNonExistingGroupUser() {
        val databaseSizeBeforeUpdate = groupUserRepository.findAll().size
        groupUser.id = count.incrementAndGet()

        val groupUserDTO = groupUserMapper.toDto(groupUser)

        restGroupUserMockMvc.perform(
            patch(ENTITY_API_URL_ID, groupUserDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(groupUserDTO))
        ).andExpect(status().isBadRequest)

        val groupUserList = groupUserRepository.findAll()
        assertThat(groupUserList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchGroupUser() {
        val databaseSizeBeforeUpdate = groupUserRepository.findAll().size
        groupUser.id = count.incrementAndGet()

        val groupUserDTO = groupUserMapper.toDto(groupUser)

        restGroupUserMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(groupUserDTO))
        )
            .andExpect(status().isBadRequest)

        val groupUserList = groupUserRepository.findAll()
        assertThat(groupUserList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamGroupUser() {
        val databaseSizeBeforeUpdate = groupUserRepository.findAll().size
        groupUser.id = count.incrementAndGet()

        val groupUserDTO = groupUserMapper.toDto(groupUser)

        restGroupUserMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(groupUserDTO))
        ).andExpect(status().isMethodNotAllowed)

        val groupUserList = groupUserRepository.findAll()
        assertThat(groupUserList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteGroupUser() {
        groupUserRepository.saveAndFlush(groupUser)
        val databaseSizeBeforeDelete = groupUserRepository.findAll().size

        restGroupUserMockMvc.perform(
            delete(ENTITY_API_URL_ID, groupUser.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        val groupUserList = groupUserRepository.findAll()
        assertThat(groupUserList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private val ENTITY_API_URL: String = "/api/group-users"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

        @JvmStatic
        fun createEntity(em: EntityManager): GroupUser {
            val groupUser = GroupUser()

            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            groupUser.user = user

            val group: Group
            if (findAll(em, Group::class).isEmpty()) {
                group = GroupResourceIT.createEntity(em)
                em.persist(group)
                em.flush()
            } else {
                group = findAll(em, Group::class)[0]
            }
            groupUser.group = group
            return groupUser
        }

        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): GroupUser {
            val groupUser = GroupUser()

            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            groupUser.user = user

            val group: Group
            if (findAll(em, Group::class).isEmpty()) {
                group = GroupResourceIT.createUpdatedEntity(em)
                em.persist(group)
                em.flush()
            } else {
                group = findAll(em, Group::class)[0]
            }
            groupUser.group = group
            return groupUser
        }
    }
}
