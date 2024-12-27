package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.repository.FieldRepository
import com.clangengineer.surveymodus.service.FieldQueryService
import com.clangengineer.surveymodus.service.FieldService
import com.clangengineer.surveymodus.service.criteria.FieldCriteria
import com.clangengineer.surveymodus.service.dto.FieldDTO
import com.clangengineer.surveymodus.web.rest.errors.BadRequestAlertException
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
class FieldResource(
    private val fieldService: FieldService,
    private val fieldRepository: FieldRepository,
    private val fieldQueryService: FieldQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "field"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PostMapping("/fields")
    fun createField(@Valid @RequestBody fieldDTO: FieldDTO): ResponseEntity<FieldDTO> {
        log.debug("REST request to save Field : $fieldDTO")

        if (fieldDTO.id != null) {
            throw BadRequestAlertException("A new field cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = fieldService.save(fieldDTO)
        return ResponseEntity.created(URI("/api/fields/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    @PutMapping("/fields/{id}")
    fun updateField(@PathVariable(value = "id", required = false) id: Long, @Valid @RequestBody fieldDTO: FieldDTO): ResponseEntity<FieldDTO> {
        log.debug("REST request to update Field : {}, {}", id, fieldDTO)

        if (fieldDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, fieldDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!fieldRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = fieldService.update(fieldDTO)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fieldDTO.id.toString()))
            .body(result)
    }

    @PatchMapping(value = ["/fields/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateField(@PathVariable(value = "id", required = false) id: Long, @NotNull @RequestBody fieldDTO: FieldDTO): ResponseEntity<FieldDTO> {
        log.debug("REST request to partial update Field partially : {}, {}", id, fieldDTO)

        if (fieldDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, fieldDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!fieldRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = fieldService.partialUpdate(fieldDTO)

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fieldDTO.id.toString()))
    }

    @GetMapping("/fields")
    fun getAllFields(criteria: FieldCriteria, @org.springdoc.api.annotations.ParameterObject pageable: Pageable): ResponseEntity<MutableList<FieldDTO>> {
        log.debug("REST request to get Fields by criteria: $criteria")

        val page = fieldQueryService.findByCriteria(criteria, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    @GetMapping("/fields/count")
    fun countFields(criteria: FieldCriteria): ResponseEntity<Long> {
        log.debug("REST request to count Fields by criteria: $criteria")
        return ResponseEntity.ok().body(fieldQueryService.countByCriteria(criteria))
    }

    @GetMapping("/fields/{id}")
    fun getField(@PathVariable id: Long): ResponseEntity<FieldDTO> {
        log.debug("REST request to get Field : $id")

        val fieldDTO = fieldService.findOne(id)
        return ResponseUtil.wrapOrNotFound(fieldDTO)
    }

    @DeleteMapping("/fields/{id}")
    fun deleteField(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Field : $id")

        fieldService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
