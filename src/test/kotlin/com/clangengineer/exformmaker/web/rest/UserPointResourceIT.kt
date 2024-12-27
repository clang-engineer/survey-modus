package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.domain.Point
import com.clangengineer.surveymodus.domain.User
import com.clangengineer.surveymodus.domain.UserPoint
import com.clangengineer.surveymodus.repository.UserPointRepository
import com.clangengineer.surveymodus.service.UserPointService
import com.clangengineer.surveymodus.service.mapper.UserPointMapper
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import java.util.Random
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.EntityManager
import kotlin.test.assertNotNull

/**
 * Integration tests for the [UserPointResource] REST controller.
 */
@IntegrationTest
@Extensions(
    ExtendWith(MockitoExtension::class)
)
@AutoConfigureMockMvc
@WithMockUser
class UserPointResourceIT {
    @Autowired
    private lateinit var userPointRepository: UserPointRepository

    @Mock
    private lateinit var userPointRepositoryMock: UserPointRepository

    @Autowired
    private lateinit var userPointMapper: UserPointMapper

    @Mock
    private lateinit var userPointServiceMock: UserPointService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restUserPointMockMvc: MockMvc

    private lateinit var userPoint: UserPoint

    @BeforeEach
    fun initTest() {
        userPoint = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createUserPoint() {
        val databaseSizeBeforeCreate = userPointRepository.findAll().size
        // Create the UserPoint
        val userPointDTO = userPointMapper.toDto(userPoint)
        restUserPointMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userPointDTO))
        ).andExpect(status().isCreated)

        // Validate the UserPoint in the database
        val userPointList = userPointRepository.findAll()
        assertThat(userPointList).hasSize(databaseSizeBeforeCreate + 1)
        val testUserPoint = userPointList[userPointList.size - 1]
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createUserPointWithExistingId() {
        // Create the UserPoint with an existing ID
        userPoint.id = 1L
        val userPointDTO = userPointMapper.toDto(userPoint)

        val databaseSizeBeforeCreate = userPointRepository.findAll().size
        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPointMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userPointDTO))
        ).andExpect(status().isBadRequest)

        // Validate the UserPoint in the database
        val userPointList = userPointRepository.findAll()
        assertThat(userPointList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserPoints() {
        // Initialize the database
        userPointRepository.saveAndFlush(userPoint)

        // Get all the userPointList
        restUserPointMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPoint.id?.toInt())))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllUserPointsWithEagerRelationshipsIsEnabled() {
        `when`(userPointServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restUserPointMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false"))
            .andExpect(status().isOk)

        verify(userPointRepositoryMock, times(1)).findAll(any(Pageable::class.java))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllUserPointsWithEagerRelationshipsIsNotEnabled() {
        `when`(userPointServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restUserPointMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true"))
            .andExpect(status().isOk)

        verify(userPointServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getUserPoint() {
        // Initialize the database
        userPointRepository.saveAndFlush(userPoint)

        val id = userPoint.id
        assertNotNull(id)

        // Get the userPoint
        restUserPointMockMvc.perform(get(ENTITY_API_URL_ID, userPoint.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userPoint.id?.toInt()))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getUserPointsByIdFiltering() {
        // Initialize the database
        userPointRepository.saveAndFlush(userPoint)
        val id = userPoint.id

        defaultUserPointShouldBeFound("id.equals=$id")
        defaultUserPointShouldNotBeFound("id.notEquals=$id")
        defaultUserPointShouldBeFound("id.greaterThanOrEqual=$id")
        defaultUserPointShouldNotBeFound("id.greaterThan=$id")

        defaultUserPointShouldBeFound("id.lessThanOrEqual=$id")
        defaultUserPointShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserPointsByUserIsEqualToSomething() {
        var user: User
        if (findAll(em, User::class).isEmpty()) {
            userPointRepository.saveAndFlush(userPoint)
            user = UserResourceIT.createEntity(em)
        } else {
            user = findAll(em, User::class)[0]
        }
        em.persist(user)
        em.flush()
        userPoint.user = user
        userPointRepository.saveAndFlush(userPoint)
        val userId = user?.id

        // Get all the userPointList where user equals to userId
        defaultUserPointShouldBeFound("userId.equals=$userId")

        // Get all the userPointList where user equals to (userId?.plus(1))
        defaultUserPointShouldNotBeFound("userId.equals=${(userId?.plus(1))}")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserPointsByPointIsEqualToSomething() {
        var point: Point
        if (findAll(em, Point::class).isEmpty()) {
            userPointRepository.saveAndFlush(userPoint)
            point = PointResourceIT.createEntity(em)
        } else {
            point = findAll(em, Point::class)[0]
        }
        em.persist(point)
        em.flush()
        userPoint.point = point
        userPointRepository.saveAndFlush(userPoint)
        val pointId = point?.id

        // Get all the userPointList where point equals to pointId
        defaultUserPointShouldBeFound("pointId.equals=$pointId")

        // Get all the userPointList where point equals to (pointId?.plus(1))
        defaultUserPointShouldNotBeFound("pointId.equals=${(pointId?.plus(1))}")
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */

    @Throws(Exception::class)
    private fun defaultUserPointShouldBeFound(filter: String) {
        restUserPointMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPoint.id?.toInt())))

        // Check, that the count call also returns 1
        restUserPointMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    @Throws(Exception::class)
    private fun defaultUserPointShouldNotBeFound(filter: String) {
        restUserPointMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        // Check, that the count call also returns 0
        restUserPointMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingUserPoint() {
        // Get the userPoint
        restUserPointMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putExistingUserPoint() {
        // Initialize the database
        userPointRepository.saveAndFlush(userPoint)

        val databaseSizeBeforeUpdate = userPointRepository.findAll().size

        // Update the userPoint
        val updatedUserPoint = userPointRepository.findById(userPoint.id).get()
        // Disconnect from session so that the updates on updatedUserPoint are not directly saved in db
        em.detach(updatedUserPoint)
        val userPointDTO = userPointMapper.toDto(updatedUserPoint)

        restUserPointMockMvc.perform(
            put(ENTITY_API_URL_ID, userPointDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userPointDTO))
        ).andExpect(status().isOk)

        // Validate the UserPoint in the database
        val userPointList = userPointRepository.findAll()
        assertThat(userPointList).hasSize(databaseSizeBeforeUpdate)
        val testUserPoint = userPointList[userPointList.size - 1]
    }

    @Test
    @Transactional
    fun putNonExistingUserPoint() {
        val databaseSizeBeforeUpdate = userPointRepository.findAll().size
        userPoint.id = count.incrementAndGet()

        // Create the UserPoint
        val userPointDTO = userPointMapper.toDto(userPoint)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPointMockMvc.perform(
            put(ENTITY_API_URL_ID, userPointDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userPointDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the UserPoint in the database
        val userPointList = userPointRepository.findAll()
        assertThat(userPointList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchUserPoint() {
        val databaseSizeBeforeUpdate = userPointRepository.findAll().size
        userPoint.id = count.incrementAndGet()

        // Create the UserPoint
        val userPointDTO = userPointMapper.toDto(userPoint)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPointMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userPointDTO))
        ).andExpect(status().isBadRequest)

        // Validate the UserPoint in the database
        val userPointList = userPointRepository.findAll()
        assertThat(userPointList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamUserPoint() {
        val databaseSizeBeforeUpdate = userPointRepository.findAll().size
        userPoint.id = count.incrementAndGet()

        // Create the UserPoint
        val userPointDTO = userPointMapper.toDto(userPoint)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPointMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userPointDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the UserPoint in the database
        val userPointList = userPointRepository.findAll()
        assertThat(userPointList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateUserPointWithPatch() {
        userPointRepository.saveAndFlush(userPoint)

        val databaseSizeBeforeUpdate = userPointRepository.findAll().size

// Update the userPoint using partial update
        val partialUpdatedUserPoint = UserPoint().apply {
            id = userPoint.id
        }

        restUserPointMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedUserPoint.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedUserPoint))
        )
            .andExpect(status().isOk)

// Validate the UserPoint in the database
        val userPointList = userPointRepository.findAll()
        assertThat(userPointList).hasSize(databaseSizeBeforeUpdate)
        val testUserPoint = userPointList.last()
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateUserPointWithPatch() {
        userPointRepository.saveAndFlush(userPoint)

        val databaseSizeBeforeUpdate = userPointRepository.findAll().size

// Update the userPoint using partial update
        val partialUpdatedUserPoint = UserPoint().apply {
            id = userPoint.id
        }

        restUserPointMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedUserPoint.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedUserPoint))
        )
            .andExpect(status().isOk)

// Validate the UserPoint in the database
        val userPointList = userPointRepository.findAll()
        assertThat(userPointList).hasSize(databaseSizeBeforeUpdate)
        val testUserPoint = userPointList.last()
    }

    @Throws(Exception::class)
    fun patchNonExistingUserPoint() {
        val databaseSizeBeforeUpdate = userPointRepository.findAll().size
        userPoint.id = count.incrementAndGet()

        // Create the UserPoint
        val userPointDTO = userPointMapper.toDto(userPoint)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPointMockMvc.perform(
            patch(ENTITY_API_URL_ID, userPointDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(userPointDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the UserPoint in the database
        val userPointList = userPointRepository.findAll()
        assertThat(userPointList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchUserPoint() {
        val databaseSizeBeforeUpdate = userPointRepository.findAll().size
        userPoint.id = count.incrementAndGet()

        // Create the UserPoint
        val userPointDTO = userPointMapper.toDto(userPoint)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPointMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(userPointDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the UserPoint in the database
        val userPointList = userPointRepository.findAll()
        assertThat(userPointList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamUserPoint() {
        val databaseSizeBeforeUpdate = userPointRepository.findAll().size
        userPoint.id = count.incrementAndGet()

        // Create the UserPoint
        val userPointDTO = userPointMapper.toDto(userPoint)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPointMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(userPointDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the UserPoint in the database
        val userPointList = userPointRepository.findAll()
        assertThat(userPointList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteUserPoint() {
        // Initialize the database
        userPointRepository.saveAndFlush(userPoint)
        val databaseSizeBeforeDelete = userPointRepository.findAll().size
        // Delete the userPoint
        restUserPointMockMvc.perform(
            delete(ENTITY_API_URL_ID, userPoint.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val userPointList = userPointRepository.findAll()
        assertThat(userPointList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private val ENTITY_API_URL: String = "/api/user-points"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): UserPoint {
            val userPoint = UserPoint()

            // Add required entity
            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            userPoint.user = user
            // Add required entity
            val point: Point
            if (findAll(em, Point::class).isEmpty()) {
                point = PointResourceIT.createEntity(em)
                em.persist(point)
                em.flush()
            } else {
                point = findAll(em, Point::class)[0]
            }
            userPoint.point = point
            return userPoint
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): UserPoint {
            val userPoint = UserPoint()

            // Add required entity
            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            userPoint.user = user
            // Add required entity
            val point: Point
            if (findAll(em, Point::class).isEmpty()) {
                point = PointResourceIT.createUpdatedEntity(em)
                em.persist(point)
                em.flush()
            } else {
                point = findAll(em, Point::class)[0]
            }
            userPoint.point = point
            return userPoint
        }
    }
}
