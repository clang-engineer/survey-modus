package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.IntegrationTest
import com.clangengineer.exformmaker.domain.Group
import com.clangengineer.exformmaker.domain.User
import com.clangengineer.exformmaker.domain.enumeration.level
import com.clangengineer.exformmaker.repository.GroupRepository
import com.clangengineer.exformmaker.service.mapper.GroupMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.EntityManager
import kotlin.test.assertNotNull

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GroupResourceIT {
    @Autowired
    private lateinit var groupRepository: GroupRepository

    @Autowired
    private lateinit var groupMapper: GroupMapper

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restGroupMockMvc: MockMvc

    private lateinit var group: Group

    @BeforeEach
    fun initTest() {
        group = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createGroup() {
        val databaseSizeBeforeCreate = groupRepository.findAll().size

        // Create the Group
        val groupDTO = groupMapper.toDto(group)
        restGroupMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupDTO))
        ).andExpect(status().isCreated)

        // Validate the Group in the database
        val groupList = groupRepository.findAll()
        assertThat(groupList).hasSize(databaseSizeBeforeCreate + 1)
        val testGroup = groupList[groupList.size - 1]

        assertThat(testGroup.title).isEqualTo(DEFAULT_TITLE)
        assertThat(testGroup.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testGroup.activated).isEqualTo(DEFAULT_ACTIVATED)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createGroupWithExistingId() {
        // Create the Group with an existing ID
        group.id = 1L
        val groupDTO = groupMapper.toDto(group)

        val databaseSizeBeforeCreate = groupRepository.findAll().size
        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Group in the database
        val groupList = groupRepository.findAll()
        assertThat(groupList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkTitleIsRequired() {
        val databaseSizeBeforeTest = groupRepository.findAll().size
        // set the field null
        group.title = null

        // Create the Group, which fails.
        val groupDTO = groupMapper.toDto(group)

        restGroupMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupDTO))
        ).andExpect(status().isBadRequest)

        val groupList = groupRepository.findAll()
        assertThat(groupList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroups() {
        // Initialize the database
        groupRepository.saveAndFlush(group)

        // Get all the groupList
        restGroupMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(group.id?.toInt())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED)))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getGroup() {
        // Initialize the database
        groupRepository.saveAndFlush(group)

        val id = group.id
        assertNotNull(id)

        // Get the group
        restGroupMockMvc.perform(get(ENTITY_API_URL_ID, group.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(group.id?.toInt()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getGroupsByIdFiltering() {
        // Initialize the database
        groupRepository.saveAndFlush(group)
        val id = group.id

        defaultGroupShouldBeFound("id.equals=$id")
        defaultGroupShouldNotBeFound("id.notEquals=$id")

        defaultGroupShouldBeFound("id.greaterThanOrEqual=$id")
        defaultGroupShouldNotBeFound("id.greaterThan=$id")

        defaultGroupShouldBeFound("id.lessThanOrEqual=$id")
        defaultGroupShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupsByTitleIsEqualToSomething() {
        // Initialize the database
        groupRepository.saveAndFlush(group)

        // Get all the groupList where title equals to DEFAULT_TITLE
        defaultGroupShouldBeFound("title.equals=$DEFAULT_TITLE")

        // Get all the groupList where title equals to UPDATED_TITLE
        defaultGroupShouldNotBeFound("title.equals=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupsByTitleIsInShouldWork() {
        // Initialize the database
        groupRepository.saveAndFlush(group)

        // Get all the groupList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultGroupShouldBeFound("title.in=$DEFAULT_TITLE,$UPDATED_TITLE")

        // Get all the groupList where title equals to UPDATED_TITLE
        defaultGroupShouldNotBeFound("title.in=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupsByTitleIsNullOrNotNull() {
        // Initialize the database
        groupRepository.saveAndFlush(group)

        // Get all the groupList where title is not null
        defaultGroupShouldBeFound("title.specified=true")

        // Get all the groupList where title is null
        defaultGroupShouldNotBeFound("title.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupsByTitleContainsSomething() {
        // Initialize the database
        groupRepository.saveAndFlush(group)

        // Get all the groupList where title contains DEFAULT_TITLE
        defaultGroupShouldBeFound("title.contains=$DEFAULT_TITLE")

        // Get all the groupList where title contains UPDATED_TITLE
        defaultGroupShouldNotBeFound("title.contains=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupsByTitleNotContainsSomething() {
        // Initialize the database
        groupRepository.saveAndFlush(group)

        // Get all the groupList where title does not contain DEFAULT_TITLE
        defaultGroupShouldNotBeFound("title.doesNotContain=$DEFAULT_TITLE")

        // Get all the groupList where title does not contain UPDATED_TITLE
        defaultGroupShouldBeFound("title.doesNotContain=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupsByDescriptionIsEqualToSomething() {
        // Initialize the database
        groupRepository.saveAndFlush(group)

        // Get all the groupList where description equals to DEFAULT_DESCRIPTION
        defaultGroupShouldBeFound("description.equals=$DEFAULT_DESCRIPTION")

        // Get all the groupList where description equals to UPDATED_DESCRIPTION
        defaultGroupShouldNotBeFound("description.equals=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupsByDescriptionIsInShouldWork() {
        // Initialize the database
        groupRepository.saveAndFlush(group)

        // Get all the groupList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultGroupShouldBeFound("description.in=$DEFAULT_DESCRIPTION,$UPDATED_DESCRIPTION")

        // Get all the groupList where description equals to UPDATED_DESCRIPTION
        defaultGroupShouldNotBeFound("description.in=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupsByDescriptionIsNullOrNotNull() {
        // Initialize the database
        groupRepository.saveAndFlush(group)

        // Get all the groupList where description is not null
        defaultGroupShouldBeFound("description.specified=true")

        // Get all the groupList where description is null
        defaultGroupShouldNotBeFound("description.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupsByDescriptionContainsSomething() {
        // Initialize the database
        groupRepository.saveAndFlush(group)

        // Get all the groupList where description contains DEFAULT_DESCRIPTION
        defaultGroupShouldBeFound("description.contains=$DEFAULT_DESCRIPTION")

        // Get all the groupList where description contains UPDATED_DESCRIPTION
        defaultGroupShouldNotBeFound("description.contains=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupsByDescriptionNotContainsSomething() {
        // Initialize the database
        groupRepository.saveAndFlush(group)

        // Get all the groupList where description does not contain DEFAULT_DESCRIPTION
        defaultGroupShouldNotBeFound("description.doesNotContain=$DEFAULT_DESCRIPTION")

        // Get all the groupList where description does not contain UPDATED_DESCRIPTION
        defaultGroupShouldBeFound("description.doesNotContain=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupsByActivatedIsEqualToSomething() {
        // Initialize the database
        groupRepository.saveAndFlush(group)

        // Get all the groupList where activated equals to DEFAULT_ACTIVATED
        defaultGroupShouldBeFound("activated.equals=$DEFAULT_ACTIVATED")

        // Get all the groupList where activated equals to UPDATED_ACTIVATED
        defaultGroupShouldNotBeFound("activated.equals=$UPDATED_ACTIVATED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupsByActivatedIsInShouldWork() {
        // Initialize the database
        groupRepository.saveAndFlush(group)

        // Get all the groupList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultGroupShouldBeFound("activated.in=$DEFAULT_ACTIVATED,$UPDATED_ACTIVATED")

        // Get all the groupList where activated equals to UPDATED_ACTIVATED
        defaultGroupShouldNotBeFound("activated.in=$UPDATED_ACTIVATED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupsByActivatedIsNullOrNotNull() {
        // Initialize the database
        groupRepository.saveAndFlush(group)

        // Get all the groupList where activated is not null
        defaultGroupShouldBeFound("activated.specified=true")

        // Get all the groupList where activated is null
        defaultGroupShouldNotBeFound("activated.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllGroupsByUserIsEqualToSomething() {
        var user: User
        if (findAll(em, User::class).isEmpty()) {
            groupRepository.saveAndFlush(group)
            user = UserResourceIT.createEntity(em)
        } else {
            user = findAll(em, User::class)[0]
        }
        em.persist(user)
        em.flush()
        group.user = user
        groupRepository.saveAndFlush(group)
        val userId = user?.id

        // Get all the pointList where user equals to userId
        defaultGroupShouldBeFound("userId.equals=$userId")

        // Get all the pointList where user equals to (userId?.plus(1))
        defaultGroupShouldNotBeFound("userId.equals=${(userId?.plus(1))}")
    }

    @Throws(Exception::class)
    private fun defaultGroupShouldBeFound(filter: String) {
        restGroupMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(group.id?.toInt())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED)))

        // Check, that the count call also returns 1
        restGroupMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    @Throws(Exception::class)
    private fun defaultGroupShouldNotBeFound(filter: String) {
        restGroupMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        // Check, that the count call also returns 0
        restGroupMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingGroup() {
        restGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun putExistingGroup() {
        groupRepository.saveAndFlush(group)

        val databaseSizeBeforeUpdate = groupRepository.findAll().size

        val updatedGroup = groupRepository.findById(group.id).get()
        em.detach(updatedGroup)
        updatedGroup.title = UPDATED_TITLE
        updatedGroup.description = UPDATED_DESCRIPTION
        updatedGroup.activated = UPDATED_ACTIVATED
        val groupDTO = groupMapper.toDto(updatedGroup)

        restGroupMockMvc.perform(
            put(ENTITY_API_URL_ID, groupDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupDTO))
        ).andExpect(status().isOk)

        // Validate the Group in the database
        val groupList = groupRepository.findAll()
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate)
        val testGroup = groupList[groupList.size - 1]
        assertThat(testGroup.title).isEqualTo(UPDATED_TITLE)
        assertThat(testGroup.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testGroup.activated).isEqualTo(UPDATED_ACTIVATED)
    }

    @Test
    @Transactional
    fun putNonExistingGroup() {
        val databaseSizeBeforeUpdate = groupRepository.findAll().size
        group.id = count.incrementAndGet()

        val groupDTO = groupMapper.toDto(group)

        restGroupMockMvc.perform(
            put(ENTITY_API_URL_ID, groupDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupDTO))
        )
            .andExpect(status().isBadRequest)

        val groupList = groupRepository.findAll()
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchGroup() {
        val databaseSizeBeforeUpdate = groupRepository.findAll().size
        group.id = count.incrementAndGet()

        val groupDTO = groupMapper.toDto(group)

        restGroupMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupDTO))
        )
            .andExpect(status().isBadRequest)

        val groupList = groupRepository.findAll()
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamGroup() {
        val databaseSizeBeforeUpdate = groupRepository.findAll().size
        group.id = count.incrementAndGet()

        val groupDTO = groupMapper.toDto(group)

        restGroupMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the Group in the database
        val groupList = groupRepository.findAll()
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateGroupWithPatch() {
        groupRepository.saveAndFlush(group)

        val databaseSizeBeforeUpdate = groupRepository.findAll().size

        val partialUpdatedGroup = Group().apply {
            id = group.id
            title = UPDATED_TITLE
            description = UPDATED_DESCRIPTION
            activated = UPDATED_ACTIVATED
        }

        restGroupMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedGroup.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedGroup))
        )
            .andExpect(status().isOk)

        val groupList = groupRepository.findAll()
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate)
        val testGroup = groupList.last()
        assertThat(testGroup.title).isEqualTo(UPDATED_TITLE)
        assertThat(testGroup.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testGroup.activated).isEqualTo(UPDATED_ACTIVATED)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateGroupWithPatch() {
        groupRepository.saveAndFlush(group)

        val databaseSizeBeforeUpdate = groupRepository.findAll().size

        val partialUpdatedGroup = Group().apply {
            id = group.id
            title = UPDATED_TITLE
            description = UPDATED_DESCRIPTION
            activated = UPDATED_ACTIVATED
        }

        restGroupMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedGroup.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedGroup))
        )
            .andExpect(status().isOk)

        val groupList = groupRepository.findAll()
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate)
        val testGroup = groupList.last()
        assertThat(testGroup.title).isEqualTo(UPDATED_TITLE)
        assertThat(testGroup.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testGroup.activated).isEqualTo(UPDATED_ACTIVATED)
    }

    @Throws(Exception::class)
    fun patchNonExistingGroup() {
        val databaseSizeBeforeUpdate = groupRepository.findAll().size
        group.id = count.incrementAndGet()

        val groupDTO = groupMapper.toDto(group)

        restGroupMockMvc.perform(
            patch(ENTITY_API_URL_ID, groupDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(groupDTO))
        )
            .andExpect(status().isBadRequest)

        val groupList = groupRepository.findAll()
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchGroup() {
        val databaseSizeBeforeUpdate = groupRepository.findAll().size
        group.id = count.incrementAndGet()

        val groupDTO = groupMapper.toDto(group)

        restGroupMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(groupDTO))
        )
            .andExpect(status().isBadRequest)

        val groupList = groupRepository.findAll()
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamGroup() {
        val databaseSizeBeforeUpdate = groupRepository.findAll().size
        group.id = count.incrementAndGet()

        val groupDTO = groupMapper.toDto(group)

        restGroupMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(groupDTO))
        ).andExpect(status().isMethodNotAllowed)

        val groupList = groupRepository.findAll()
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteGroup() {
        groupRepository.saveAndFlush(group)
        val databaseSizeBeforeDelete = groupRepository.findAll().size

        restGroupMockMvc.perform(
            delete(ENTITY_API_URL_ID, group.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        val groupList = groupRepository.findAll()
        assertThat(groupList).hasSize(databaseSizeBeforeDelete - 1)
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

        private val ENTITY_API_URL: String = "/api/groups"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

        @JvmStatic
        fun createEntity(em: EntityManager): Group {
            val group = Group(
                title = DEFAULT_TITLE,
                description = DEFAULT_DESCRIPTION,
                activated = DEFAULT_ACTIVATED,
            )

            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            group.user = user
            return group

            return group
        }

        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Group {
            val group = Group(
                title = UPDATED_TITLE,
                description = UPDATED_DESCRIPTION,
                activated = UPDATED_ACTIVATED,
            )

            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            group.user = user
            return group

            return group
        }
    }
}
