package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.repository.FormRepository
import com.clangengineer.exformmaker.service.FormQueryService
import com.clangengineer.exformmaker.service.FormService
import com.clangengineer.exformmaker.service.criteria.FormCriteria
import com.clangengineer.exformmaker.service.dto.FormDTO
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
class FormResource(
    private val formService: FormService,
    private val formRepository: FormRepository,
    private val formQueryService: FormQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "form"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PostMapping("/forms")
    fun createForm(@Valid @RequestBody formDTO: FormDTO): ResponseEntity<FormDTO> {
        log.debug("REST request to save Form : $formDTO")

        if (formDTO.id != null) {
            throw BadRequestAlertException("A new form cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = formService.save(formDTO)
        return ResponseEntity.created(URI("/api/forms/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    @PutMapping("/forms/{id}")
    fun updateForm(@PathVariable(value = "id", required = false) id: Long, @Valid @RequestBody formDTO: FormDTO): ResponseEntity<FormDTO> {
        log.debug("REST request to update Form : {}, {}", id, formDTO)

        if (formDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, formDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!formRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = formService.update(formDTO)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formDTO.id.toString()))
            .body(result)
    }

    @PatchMapping(value = ["/forms/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateForm(@PathVariable(value = "id", required = false) id: Long, @NotNull @RequestBody formDTO: FormDTO): ResponseEntity<FormDTO> {
        log.debug("REST request to partial update Form partially : {}, {}", id, formDTO)

        if (formDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, formDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!formRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = formService.partialUpdate(formDTO)

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formDTO.id.toString()))
    }

    @GetMapping("/forms")
    fun getAllForms(criteria: FormCriteria, @org.springdoc.api.annotations.ParameterObject pageable: Pageable): ResponseEntity<MutableList<FormDTO>> {
        log.debug("REST request to get Forms by criteria: $criteria")

        val page = formQueryService.findByCriteria(criteria, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    @GetMapping("/forms/count")
    fun countForms(criteria: FormCriteria): ResponseEntity<Long> {
        log.debug("REST request to count Forms by criteria: $criteria")
        return ResponseEntity.ok().body(formQueryService.countByCriteria(criteria))
    }

    @GetMapping("/forms/{id}")
    fun getForm(@PathVariable id: Long): ResponseEntity<FormDTO> {
        log.debug("REST request to get Form : $id")

        val formDTO = formService.findOne(id)
        return ResponseUtil.wrapOrNotFound(formDTO)
    }

    @DeleteMapping("/forms/{id}")
    fun deleteForm(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Form : $id")

        formService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
