package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.IntegrationTest
import com.clangengineer.exformmaker.domain.Category
import com.clangengineer.exformmaker.domain.enumeration.level
import com.clangengineer.exformmaker.repository.CategoryRepository
import com.clangengineer.exformmaker.service.mapper.CategoryMapper
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
class CategoryResourceIT {
  @Autowired
  private lateinit var categoryRepository: CategoryRepository

  @Autowired
  private lateinit var categoryMapper: CategoryMapper

  @Autowired
  private lateinit var em: EntityManager

  @Autowired
  private lateinit var restCategoryMockMvc: MockMvc

  private lateinit var category: Category

  @BeforeEach
  fun initTest() {
    category = createEntity(em)
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun createCategory() {
    val databaseSizeBeforeCreate = categoryRepository.findAll().size

    val categoryDTO = categoryMapper.toDto(category)
    restCategoryMockMvc.perform(
      post(ENTITY_API_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertObjectToJsonBytes(categoryDTO))
    ).andExpect(status().isCreated)

    val categoryList = categoryRepository.findAll()
    assertThat(categoryList).hasSize(databaseSizeBeforeCreate + 1)
    val testCategory = categoryList[categoryList.size - 1]

    assertThat(testCategory.title).isEqualTo(DEFAULT_TITLE)
    assertThat(testCategory.description).isEqualTo(DEFAULT_DESCRIPTION)
    assertThat(testCategory.activated).isEqualTo(DEFAULT_ACTIVATED)
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun createCategoryWithExistingId() {
    category.id = 1L
    val categoryDTO = categoryMapper.toDto(category)

    val databaseSizeBeforeCreate = categoryRepository.findAll().size

    restCategoryMockMvc.perform(
      post(ENTITY_API_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertObjectToJsonBytes(categoryDTO))
    ).andExpect(status().isBadRequest)

    val categoryList = categoryRepository.findAll()
    assertThat(categoryList).hasSize(databaseSizeBeforeCreate)
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun checkTitleIsRequired() {
    val databaseSizeBeforeTest = categoryRepository.findAll().size

    category.title = null

    val categoryDTO = categoryMapper.toDto(category)

    restCategoryMockMvc.perform(
      post(ENTITY_API_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertObjectToJsonBytes(categoryDTO))
    ).andExpect(status().isBadRequest)

    val categoryList = categoryRepository.findAll()
    assertThat(categoryList).hasSize(databaseSizeBeforeTest)
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getAllCategorys() {
    categoryRepository.saveAndFlush(category)

    restCategoryMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(category.id?.toInt())))
      .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
      .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
      .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED)))
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getCategory() {
    categoryRepository.saveAndFlush(category)

    val id = category.id
    assertNotNull(id)

    restCategoryMockMvc.perform(get(ENTITY_API_URL_ID, category.id))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(category.id?.toInt()))
      .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
      .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
      .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED))
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getCategorysByIdFiltering() {
    categoryRepository.saveAndFlush(category)
    val id = category.id

    defaultCategoryShouldBeFound("id.equals=$id")
    defaultCategoryShouldNotBeFound("id.notEquals=$id")

    defaultCategoryShouldBeFound("id.greaterThanOrEqual=$id")
    defaultCategoryShouldNotBeFound("id.greaterThan=$id")

    defaultCategoryShouldBeFound("id.lessThanOrEqual=$id")
    defaultCategoryShouldNotBeFound("id.lessThan=$id")
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getAllCategorysByTitleIsEqualToSomething() {
    categoryRepository.saveAndFlush(category)

    defaultCategoryShouldBeFound("title.equals=$DEFAULT_TITLE")

    defaultCategoryShouldNotBeFound("title.equals=$UPDATED_TITLE")
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getAllCategorysByTitleIsInShouldWork() {
    categoryRepository.saveAndFlush(category)

    defaultCategoryShouldBeFound("title.in=$DEFAULT_TITLE,$UPDATED_TITLE")

    defaultCategoryShouldNotBeFound("title.in=$UPDATED_TITLE")
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getAllCategorysByTitleIsNullOrNotNull() {
    categoryRepository.saveAndFlush(category)

    defaultCategoryShouldBeFound("title.specified=true")

    defaultCategoryShouldNotBeFound("title.specified=false")
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getAllCategorysByTitleContainsSomething() {
    categoryRepository.saveAndFlush(category)

    defaultCategoryShouldBeFound("title.contains=$DEFAULT_TITLE")

    defaultCategoryShouldNotBeFound("title.contains=$UPDATED_TITLE")
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getAllCategorysByTitleNotContainsSomething() {
    categoryRepository.saveAndFlush(category)

    defaultCategoryShouldNotBeFound("title.doesNotContain=$DEFAULT_TITLE")

    defaultCategoryShouldBeFound("title.doesNotContain=$UPDATED_TITLE")
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getAllCategorysByDescriptionIsEqualToSomething() {
    categoryRepository.saveAndFlush(category)

    defaultCategoryShouldBeFound("description.equals=$DEFAULT_DESCRIPTION")

    defaultCategoryShouldNotBeFound("description.equals=$UPDATED_DESCRIPTION")
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getAllCategorysByDescriptionIsInShouldWork() {
    categoryRepository.saveAndFlush(category)

    defaultCategoryShouldBeFound("description.in=$DEFAULT_DESCRIPTION,$UPDATED_DESCRIPTION")

    defaultCategoryShouldNotBeFound("description.in=$UPDATED_DESCRIPTION")
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getAllCategorysByDescriptionIsNullOrNotNull() {
    categoryRepository.saveAndFlush(category)

    defaultCategoryShouldBeFound("description.specified=true")

    defaultCategoryShouldNotBeFound("description.specified=false")
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getAllCategorysByDescriptionContainsSomething() {
    categoryRepository.saveAndFlush(category)

    defaultCategoryShouldBeFound("description.contains=$DEFAULT_DESCRIPTION")

    defaultCategoryShouldNotBeFound("description.contains=$UPDATED_DESCRIPTION")
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getAllCategorysByDescriptionNotContainsSomething() {
    categoryRepository.saveAndFlush(category)

    defaultCategoryShouldNotBeFound("description.doesNotContain=$DEFAULT_DESCRIPTION")

    defaultCategoryShouldBeFound("description.doesNotContain=$UPDATED_DESCRIPTION")
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getAllCategorysByActivatedIsEqualToSomething() {
    categoryRepository.saveAndFlush(category)

    defaultCategoryShouldBeFound("activated.equals=$DEFAULT_ACTIVATED")

    defaultCategoryShouldNotBeFound("activated.equals=$UPDATED_ACTIVATED")
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getAllCategorysByActivatedIsInShouldWork() {
    categoryRepository.saveAndFlush(category)

    defaultCategoryShouldBeFound("activated.in=$DEFAULT_ACTIVATED,$UPDATED_ACTIVATED")

    defaultCategoryShouldNotBeFound("activated.in=$UPDATED_ACTIVATED")
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getAllCategorysByActivatedIsNullOrNotNull() {
    categoryRepository.saveAndFlush(category)

    defaultCategoryShouldBeFound("activated.specified=true")

    defaultCategoryShouldNotBeFound("activated.specified=false")
  }

  @Throws(Exception::class)
  private fun defaultCategoryShouldBeFound(filter: String) {
    restCategoryMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(category.id?.toInt())))
      .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
      .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
      .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED)))

    restCategoryMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(content().string("1"))
  }

  @Throws(Exception::class)
  private fun defaultCategoryShouldNotBeFound(filter: String) {
    restCategoryMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$").isArray)
      .andExpect(jsonPath("$").isEmpty)

    restCategoryMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(content().string("0"))
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun getNonExistingCategory() {
    restCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
      .andExpect(status().isNotFound)
  }

  @Test
  @Transactional
  fun putExistingCategory() {
    categoryRepository.saveAndFlush(category)

    val databaseSizeBeforeUpdate = categoryRepository.findAll().size

    val updatedCategory = categoryRepository.findById(category.id).get()
    em.detach(updatedCategory)
    updatedCategory.title = UPDATED_TITLE
    updatedCategory.description = UPDATED_DESCRIPTION
    updatedCategory.activated = UPDATED_ACTIVATED
    val categoryDTO = categoryMapper.toDto(updatedCategory)

    restCategoryMockMvc.perform(
      put(ENTITY_API_URL_ID, categoryDTO.id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertObjectToJsonBytes(categoryDTO))
    ).andExpect(status().isOk)

    val categoryList = categoryRepository.findAll()
    assertThat(categoryList).hasSize(databaseSizeBeforeUpdate)
    val testCategory = categoryList[categoryList.size - 1]
    assertThat(testCategory.title).isEqualTo(UPDATED_TITLE)
    assertThat(testCategory.description).isEqualTo(UPDATED_DESCRIPTION)
    assertThat(testCategory.activated).isEqualTo(UPDATED_ACTIVATED)
  }

  @Test
  @Transactional
  fun putNonExistingCategory() {
    val databaseSizeBeforeUpdate = categoryRepository.findAll().size
    category.id = count.incrementAndGet()

    val categoryDTO = categoryMapper.toDto(category)

    restCategoryMockMvc.perform(
      put(ENTITY_API_URL_ID, categoryDTO.id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertObjectToJsonBytes(categoryDTO))
    )
      .andExpect(status().isBadRequest)

    val categoryList = categoryRepository.findAll()
    assertThat(categoryList).hasSize(databaseSizeBeforeUpdate)
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun putWithIdMismatchCategory() {
    val databaseSizeBeforeUpdate = categoryRepository.findAll().size
    category.id = count.incrementAndGet()

    val categoryDTO = categoryMapper.toDto(category)

    restCategoryMockMvc.perform(
      put(ENTITY_API_URL_ID, count.incrementAndGet())
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertObjectToJsonBytes(categoryDTO))
    )
      .andExpect(status().isBadRequest)

    val categoryList = categoryRepository.findAll()
    assertThat(categoryList).hasSize(databaseSizeBeforeUpdate)
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun putWithMissingIdPathParamCategory() {
    val databaseSizeBeforeUpdate = categoryRepository.findAll().size
    category.id = count.incrementAndGet()

    val categoryDTO = categoryMapper.toDto(category)

    restCategoryMockMvc.perform(
      put(ENTITY_API_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertObjectToJsonBytes(categoryDTO))
    )
      .andExpect(status().isMethodNotAllowed)

    val categoryList = categoryRepository.findAll()
    assertThat(categoryList).hasSize(databaseSizeBeforeUpdate)
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun partialUpdateCategoryWithPatch() {
    categoryRepository.saveAndFlush(category)

    val databaseSizeBeforeUpdate = categoryRepository.findAll().size

    val partialUpdatedCategory = Category().apply {
      id = category.id
      title = UPDATED_TITLE
      description = UPDATED_DESCRIPTION
      activated = UPDATED_ACTIVATED
    }

    restCategoryMockMvc.perform(
      patch(ENTITY_API_URL_ID, partialUpdatedCategory.id)
        .contentType("application/merge-patch+json")
        .content(convertObjectToJsonBytes(partialUpdatedCategory))
    )
      .andExpect(status().isOk)

    val categoryList = categoryRepository.findAll()
    assertThat(categoryList).hasSize(databaseSizeBeforeUpdate)
    val testCategory = categoryList.last()
    assertThat(testCategory.title).isEqualTo(UPDATED_TITLE)
    assertThat(testCategory.description).isEqualTo(UPDATED_DESCRIPTION)
    assertThat(testCategory.activated).isEqualTo(UPDATED_ACTIVATED)
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun fullUpdateCategoryWithPatch() {
    categoryRepository.saveAndFlush(category)

    val databaseSizeBeforeUpdate = categoryRepository.findAll().size

    val partialUpdatedCategory = Category().apply {
      id = category.id
      title = UPDATED_TITLE
      description = UPDATED_DESCRIPTION
      activated = UPDATED_ACTIVATED
    }

    restCategoryMockMvc.perform(
      patch(ENTITY_API_URL_ID, partialUpdatedCategory.id)
        .contentType("application/merge-patch+json")
        .content(convertObjectToJsonBytes(partialUpdatedCategory))
    )
      .andExpect(status().isOk)

    val categoryList = categoryRepository.findAll()
    assertThat(categoryList).hasSize(databaseSizeBeforeUpdate)
    val testCategory = categoryList.last()
    assertThat(testCategory.title).isEqualTo(UPDATED_TITLE)
    assertThat(testCategory.description).isEqualTo(UPDATED_DESCRIPTION)
    assertThat(testCategory.activated).isEqualTo(UPDATED_ACTIVATED)
  }

  @Throws(Exception::class)
  fun patchNonExistingCategory() {
    val databaseSizeBeforeUpdate = categoryRepository.findAll().size
    category.id = count.incrementAndGet()

    val categoryDTO = categoryMapper.toDto(category)

    restCategoryMockMvc.perform(
      patch(ENTITY_API_URL_ID, categoryDTO.id)
        .contentType("application/merge-patch+json")
        .content(convertObjectToJsonBytes(categoryDTO))
    )
      .andExpect(status().isBadRequest)

    val categoryList = categoryRepository.findAll()
    assertThat(categoryList).hasSize(databaseSizeBeforeUpdate)
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun patchWithIdMismatchCategory() {
    val databaseSizeBeforeUpdate = categoryRepository.findAll().size
    category.id = count.incrementAndGet()

    val categoryDTO = categoryMapper.toDto(category)

    restCategoryMockMvc.perform(
      patch(ENTITY_API_URL_ID, count.incrementAndGet())
        .contentType("application/merge-patch+json")
        .content(convertObjectToJsonBytes(categoryDTO))
    )
      .andExpect(status().isBadRequest)

    val categoryList = categoryRepository.findAll()
    assertThat(categoryList).hasSize(databaseSizeBeforeUpdate)
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun patchWithMissingIdPathParamCategory() {
    val databaseSizeBeforeUpdate = categoryRepository.findAll().size
    category.id = count.incrementAndGet()

    val categoryDTO = categoryMapper.toDto(category)

    restCategoryMockMvc.perform(
      patch(ENTITY_API_URL)
        .contentType("application/merge-patch+json")
        .content(convertObjectToJsonBytes(categoryDTO))
    ).andExpect(status().isMethodNotAllowed)

    val categoryList = categoryRepository.findAll()
    assertThat(categoryList).hasSize(databaseSizeBeforeUpdate)
  }

  @Test
  @Transactional
  @Throws(Exception::class)
  fun deleteCategory() {
    categoryRepository.saveAndFlush(category)
    val databaseSizeBeforeDelete = categoryRepository.findAll().size

    restCategoryMockMvc.perform(
      delete(ENTITY_API_URL_ID, category.id)
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isNoContent)

    val categoryList = categoryRepository.findAll()
    assertThat(categoryList).hasSize(databaseSizeBeforeDelete - 1)
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

    private val ENTITY_API_URL: String = "/api/categorys"
    private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

    private val random: Random = Random()
    private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

    @JvmStatic
    fun createEntity(em: EntityManager): Category {
      val category = Category(
        title = DEFAULT_TITLE,
        description = DEFAULT_DESCRIPTION,
        activated = DEFAULT_ACTIVATED,
      )

      return category
    }

    @JvmStatic
    fun createUpdatedEntity(em: EntityManager): Category {
      val category = Category(
        title = UPDATED_TITLE,
        description = UPDATED_DESCRIPTION,
        activated = UPDATED_ACTIVATED,
      )

      return category
    }
  }
}
