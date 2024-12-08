package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.repository.CategoryRepository
import com.clangengineer.exformmaker.service.CategoryQueryService
import com.clangengineer.exformmaker.service.CategoryService
import com.clangengineer.exformmaker.service.criteria.CategoryCriteria
import com.clangengineer.exformmaker.service.dto.CategoryDTO
import com.clangengineer.exformmaker.web.rest.errors.BadRequestAlertException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.PaginationUtil
import tech.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api")
class CategoryResource(
    private val categoryService: CategoryService,
    private val categoryRepository: CategoryRepository,
    private val categoryQueryService: CategoryQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "category"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PostMapping("/categorys")
    fun createCategory(@Valid @RequestBody categoryDTO: CategoryDTO): ResponseEntity<CategoryDTO> {
        log.debug("REST request to save Category : $categoryDTO")

        if (categoryDTO.id != null) {
            throw BadRequestAlertException("A new category cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = categoryService.save(categoryDTO)
        return ResponseEntity.created(URI("/api/categorys/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    @PutMapping("/categorys/{id}")
    fun updateCategory(@PathVariable(value = "id", required = false) id: Long, @Valid @RequestBody categoryDTO: CategoryDTO): ResponseEntity<CategoryDTO> {
        log.debug("REST request to update Category : {}, {}", id, categoryDTO)

        if (categoryDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, categoryDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!categoryRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = categoryService.update(categoryDTO)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryDTO.id.toString()))
            .body(result)
    }

    @PatchMapping(value = ["/categorys/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateCategory(@PathVariable(value = "id", required = false) id: Long, @NotNull @RequestBody categoryDTO: CategoryDTO): ResponseEntity<CategoryDTO> {
        log.debug("REST request to partial update Category partially : {}, {}", id, categoryDTO)

        if (categoryDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, categoryDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!categoryRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = categoryService.partialUpdate(categoryDTO)

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryDTO.id.toString()))
    }

    @GetMapping("/categorys")
    fun getAllCategorys(criteria: CategoryCriteria, @org.springdoc.api.annotations.ParameterObject pageable: Pageable): ResponseEntity<MutableList<CategoryDTO>> {
        log.debug("REST request to get Categorys by criteria: $criteria")

        val page = categoryQueryService.findByCriteria(criteria, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    @GetMapping("/categorys/count")
    fun countCategorys(criteria: CategoryCriteria): ResponseEntity<Long> {
        log.debug("REST request to count Categorys by criteria: $criteria")
        return ResponseEntity.ok().body(categoryQueryService.countByCriteria(criteria))
    }

    @GetMapping("/categorys/{id}")
    fun getCategory(@PathVariable id: Long): ResponseEntity<CategoryDTO> {
        log.debug("REST request to get Category : $id")

        val categoryDTO = categoryService.findOne(id)
        return ResponseUtil.wrapOrNotFound(categoryDTO)
    }

    @DeleteMapping("/categorys/{id}")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Category : $id")

        categoryService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
