package com.clangengineer.exformmaker.web.rest


import com.clangengineer.exformmaker.IntegrationTest
import com.clangengineer.exformmaker.domain.Point
import com.clangengineer.exformmaker.domain.enumeration.level
import com.clangengineer.exformmaker.repository.PointRepository
import com.clangengineer.exformmaker.service.mapper.PointMapper
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

/**
 * Integration tests for the [PointResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PointResourceIT {
    @Autowired
    private lateinit var pointRepository: PointRepository

    @Autowired
    private lateinit var pointMapper: PointMapper

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator


    @Autowired
    private lateinit var em: EntityManager


    @Autowired
    private lateinit var restPointMockMvc: MockMvc

    private lateinit var point: Point


    @BeforeEach
    fun initTest() {
        point = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createPoint() {
        val databaseSizeBeforeCreate = pointRepository.findAll().size
        // Create the Point
        val pointDTO = pointMapper.toDto(point)
        restPointMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(pointDTO))
        ).andExpect(status().isCreated)

        // Validate the Point in the database
        val pointList = pointRepository.findAll()
        assertThat(pointList).hasSize(databaseSizeBeforeCreate + 1)
        val testPoint = pointList[pointList.size - 1]

        assertThat(testPoint.title).isEqualTo(DEFAULT_TITLE)
        assertThat(testPoint.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testPoint.activated).isEqualTo(DEFAULT_ACTIVATED)
        assertThat(testPoint.type).isEqualTo(DEFAULT_TYPE)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createPointWithExistingId() {
        // Create the Point with an existing ID
        point.id = 1L
        val pointDTO = pointMapper.toDto(point)

        val databaseSizeBeforeCreate = pointRepository.findAll().size
        // An entity with an existing ID cannot be created, so this API call must fail
        restPointMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(pointDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Point in the database
        val pointList = pointRepository.findAll()
        assertThat(pointList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkTitleIsRequired() {
        val databaseSizeBeforeTest = pointRepository.findAll().size
        // set the field null
        point.title = null

        // Create the Point, which fails.
        val pointDTO = pointMapper.toDto(point)

        restPointMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(pointDTO))
        ).andExpect(status().isBadRequest)

        val pointList = pointRepository.findAll()
        assertThat(pointList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPoints() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList
        restPointMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(point.id?.toInt())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getPoint() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        val id = point.id
        assertNotNull(id)

        // Get the point
        restPointMockMvc.perform(get(ENTITY_API_URL_ID, point.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(point.id?.toInt()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getPointsByIdFiltering() {
        // Initialize the database
        pointRepository.saveAndFlush(point)
        val id = point.id

        defaultPointShouldBeFound("id.equals=$id")
        defaultPointShouldNotBeFound("id.notEquals=$id")
        defaultPointShouldBeFound("id.greaterThanOrEqual=$id")
        defaultPointShouldNotBeFound("id.greaterThan=$id")

        defaultPointShouldBeFound("id.lessThanOrEqual=$id")
        defaultPointShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByTitleIsEqualToSomething() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where title equals to DEFAULT_TITLE
        defaultPointShouldBeFound("title.equals=$DEFAULT_TITLE")

        // Get all the pointList where title equals to UPDATED_TITLE
        defaultPointShouldNotBeFound("title.equals=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByTitleIsInShouldWork() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultPointShouldBeFound("title.in=$DEFAULT_TITLE,$UPDATED_TITLE")

        // Get all the pointList where title equals to UPDATED_TITLE
        defaultPointShouldNotBeFound("title.in=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByTitleIsNullOrNotNull() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where title is not null
        defaultPointShouldBeFound("title.specified=true")

        // Get all the pointList where title is null
        defaultPointShouldNotBeFound("title.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByTitleContainsSomething() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where title contains DEFAULT_TITLE
        defaultPointShouldBeFound("title.contains=$DEFAULT_TITLE")

        // Get all the pointList where title contains UPDATED_TITLE
        defaultPointShouldNotBeFound("title.contains=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByTitleNotContainsSomething() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where title does not contain DEFAULT_TITLE
        defaultPointShouldNotBeFound("title.doesNotContain=$DEFAULT_TITLE")

        // Get all the pointList where title does not contain UPDATED_TITLE
        defaultPointShouldBeFound("title.doesNotContain=$UPDATED_TITLE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByDescriptionIsEqualToSomething() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where description equals to DEFAULT_DESCRIPTION
        defaultPointShouldBeFound("description.equals=$DEFAULT_DESCRIPTION")

        // Get all the pointList where description equals to UPDATED_DESCRIPTION
        defaultPointShouldNotBeFound("description.equals=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByDescriptionIsInShouldWork() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPointShouldBeFound("description.in=$DEFAULT_DESCRIPTION,$UPDATED_DESCRIPTION")

        // Get all the pointList where description equals to UPDATED_DESCRIPTION
        defaultPointShouldNotBeFound("description.in=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByDescriptionIsNullOrNotNull() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where description is not null
        defaultPointShouldBeFound("description.specified=true")

        // Get all the pointList where description is null
        defaultPointShouldNotBeFound("description.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByDescriptionContainsSomething() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where description contains DEFAULT_DESCRIPTION
        defaultPointShouldBeFound("description.contains=$DEFAULT_DESCRIPTION")

        // Get all the pointList where description contains UPDATED_DESCRIPTION
        defaultPointShouldNotBeFound("description.contains=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByDescriptionNotContainsSomething() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where description does not contain DEFAULT_DESCRIPTION
        defaultPointShouldNotBeFound("description.doesNotContain=$DEFAULT_DESCRIPTION")

        // Get all the pointList where description does not contain UPDATED_DESCRIPTION
        defaultPointShouldBeFound("description.doesNotContain=$UPDATED_DESCRIPTION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByActivatedIsEqualToSomething() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where activated equals to DEFAULT_ACTIVATED
        defaultPointShouldBeFound("activated.equals=$DEFAULT_ACTIVATED")

        // Get all the pointList where activated equals to UPDATED_ACTIVATED
        defaultPointShouldNotBeFound("activated.equals=$UPDATED_ACTIVATED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByActivatedIsInShouldWork() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultPointShouldBeFound("activated.in=$DEFAULT_ACTIVATED,$UPDATED_ACTIVATED")

        // Get all the pointList where activated equals to UPDATED_ACTIVATED
        defaultPointShouldNotBeFound("activated.in=$UPDATED_ACTIVATED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByActivatedIsNullOrNotNull() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where activated is not null
        defaultPointShouldBeFound("activated.specified=true")

        // Get all the pointList where activated is null
        defaultPointShouldNotBeFound("activated.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByTypeIsEqualToSomething() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where type equals to DEFAULT_TYPE
        defaultPointShouldBeFound("type.equals=$DEFAULT_TYPE")

        // Get all the pointList where type equals to UPDATED_TYPE
        defaultPointShouldNotBeFound("type.equals=$UPDATED_TYPE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByTypeIsInShouldWork() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultPointShouldBeFound("type.in=$DEFAULT_TYPE,$UPDATED_TYPE")

        // Get all the pointList where type equals to UPDATED_TYPE
        defaultPointShouldNotBeFound("type.in=$UPDATED_TYPE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPointsByTypeIsNullOrNotNull() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        // Get all the pointList where type is not null
        defaultPointShouldBeFound("type.specified=true")

        // Get all the pointList where type is null
        defaultPointShouldNotBeFound("type.specified=false")
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */

    @Throws(Exception::class)
    private fun defaultPointShouldBeFound(filter: String) {
        restPointMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(point.id?.toInt())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))


        // Check, that the count call also returns 1
        restPointMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }


    /**
     * Executes the search, and checks that the default entity is not returned
     */
    @Throws(Exception::class)
    private fun defaultPointShouldNotBeFound(filter: String) {
        restPointMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        // Check, that the count call also returns 0
        restPointMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingPoint() {
        // Get the point
        restPointMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun putExistingPoint() {
        // Initialize the database
        pointRepository.saveAndFlush(point)

        val databaseSizeBeforeUpdate = pointRepository.findAll().size

        // Update the point
        val updatedPoint = pointRepository.findById(point.id).get()
        // Disconnect from session so that the updates on updatedPoint are not directly saved in db
        em.detach(updatedPoint)
        updatedPoint.title = UPDATED_TITLE
        updatedPoint.description = UPDATED_DESCRIPTION
        updatedPoint.activated = UPDATED_ACTIVATED
        updatedPoint.type = UPDATED_TYPE
        val pointDTO = pointMapper.toDto(updatedPoint)

        restPointMockMvc.perform(
            put(ENTITY_API_URL_ID, pointDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(pointDTO))
        ).andExpect(status().isOk)

        // Validate the Point in the database
        val pointList = pointRepository.findAll()
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate)
        val testPoint = pointList[pointList.size - 1]
        assertThat(testPoint.title).isEqualTo(UPDATED_TITLE)
        assertThat(testPoint.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testPoint.activated).isEqualTo(UPDATED_ACTIVATED)
        assertThat(testPoint.type).isEqualTo(UPDATED_TYPE)
    }

    @Test
    @Transactional
    fun putNonExistingPoint() {
        val databaseSizeBeforeUpdate = pointRepository.findAll().size
        point.id = count.incrementAndGet()

        // Create the Point
        val pointDTO = pointMapper.toDto(point)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointMockMvc.perform(put(ENTITY_API_URL_ID, pointDTO.id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(pointDTO)))
            .andExpect(status().isBadRequest)

        // Validate the Point in the database
        val pointList = pointRepository.findAll()
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchPoint() {
        val databaseSizeBeforeUpdate = pointRepository.findAll().size
        point.id = count.incrementAndGet()

        // Create the Point
        val pointDTO = pointMapper.toDto(point)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(pointDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Point in the database
        val pointList = pointRepository.findAll()
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamPoint() {
        val databaseSizeBeforeUpdate = pointRepository.findAll().size
        point.id = count.incrementAndGet()

        // Create the Point
        val pointDTO = pointMapper.toDto(point)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointMockMvc.perform(put(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(pointDTO)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the Point in the database
        val pointList = pointRepository.findAll()
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdatePointWithPatch() {
        pointRepository.saveAndFlush(point)


        val databaseSizeBeforeUpdate = pointRepository.findAll().size

// Update the point using partial update
        val partialUpdatedPoint = Point().apply {
            id = point.id


            title = UPDATED_TITLE
            description = UPDATED_DESCRIPTION
            activated = UPDATED_ACTIVATED
            type = UPDATED_TYPE
        }


        restPointMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedPoint.id)
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(partialUpdatedPoint)))
            .andExpect(status().isOk)

// Validate the Point in the database
        val pointList = pointRepository.findAll()
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate)
        val testPoint = pointList.last()
        assertThat(testPoint.title).isEqualTo(UPDATED_TITLE)
        assertThat(testPoint.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testPoint.activated).isEqualTo(UPDATED_ACTIVATED)
        assertThat(testPoint.type).isEqualTo(UPDATED_TYPE)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdatePointWithPatch() {
        pointRepository.saveAndFlush(point)


        val databaseSizeBeforeUpdate = pointRepository.findAll().size

// Update the point using partial update
        val partialUpdatedPoint = Point().apply {
            id = point.id


            title = UPDATED_TITLE
            description = UPDATED_DESCRIPTION
            activated = UPDATED_ACTIVATED
            type = UPDATED_TYPE
        }


        restPointMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedPoint.id)
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(partialUpdatedPoint)))
            .andExpect(status().isOk)

// Validate the Point in the database
        val pointList = pointRepository.findAll()
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate)
        val testPoint = pointList.last()
        assertThat(testPoint.title).isEqualTo(UPDATED_TITLE)
        assertThat(testPoint.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testPoint.activated).isEqualTo(UPDATED_ACTIVATED)
        assertThat(testPoint.type).isEqualTo(UPDATED_TYPE)
    }

    @Throws(Exception::class)
    fun patchNonExistingPoint() {
        val databaseSizeBeforeUpdate = pointRepository.findAll().size
        point.id = count.incrementAndGet()

        // Create the Point
        val pointDTO = pointMapper.toDto(point)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointMockMvc.perform(patch(ENTITY_API_URL_ID, pointDTO.id)
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(pointDTO)))
            .andExpect(status().isBadRequest)

        // Validate the Point in the database
        val pointList = pointRepository.findAll()
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchPoint() {
        val databaseSizeBeforeUpdate = pointRepository.findAll().size
        point.id = count.incrementAndGet()

        // Create the Point
        val pointDTO = pointMapper.toDto(point)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointMockMvc.perform(patch(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(pointDTO)))
            .andExpect(status().isBadRequest)

        // Validate the Point in the database
        val pointList = pointRepository.findAll()
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamPoint() {
        val databaseSizeBeforeUpdate = pointRepository.findAll().size
        point.id = count.incrementAndGet()

        // Create the Point
        val pointDTO = pointMapper.toDto(point)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointMockMvc.perform(patch(ENTITY_API_URL)
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(pointDTO)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the Point in the database
        val pointList = pointRepository.findAll()
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deletePoint() {
        // Initialize the database
        pointRepository.saveAndFlush(point)
        val databaseSizeBeforeDelete = pointRepository.findAll().size
        // Delete the point
        restPointMockMvc.perform(
            delete(ENTITY_API_URL_ID, point.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val pointList = pointRepository.findAll()
        assertThat(pointList).hasSize(databaseSizeBeforeDelete - 1)
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


        private val ENTITY_API_URL: String = "/api/points"
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
        fun createEntity(em: EntityManager): Point {
            val point = Point(
                title = DEFAULT_TITLE,

                description = DEFAULT_DESCRIPTION,

                activated = DEFAULT_ACTIVATED,

                type = DEFAULT_TYPE

            )


            return point
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Point {
            val point = Point(
                title = UPDATED_TITLE,

                description = UPDATED_DESCRIPTION,

                activated = UPDATED_ACTIVATED,

                type = UPDATED_TYPE

            )


            return point
        }

    }
}
