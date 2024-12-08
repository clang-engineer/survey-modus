package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.repository.CompanyFormRepository
import com.clangengineer.exformmaker.service.CompanyFormQueryService
import com.clangengineer.exformmaker.service.CompanyFormService
import com.clangengineer.exformmaker.service.criteria.CompanyFormCriteria
import com.clangengineer.exformmaker.service.dto.CompanyFormDTO
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
class CompanyFormResource(
    private val companyFormService: CompanyFormService,
    private val companyFormRepository: CompanyFormRepository,
    private val companyFormQueryService: CompanyFormQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "companyForm"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PostMapping("/company-forms")
    fun createcompanyForm(@Valid @RequestBody companyFormDTO: CompanyFormDTO): ResponseEntity<CompanyFormDTO> {
        log.debug("REST request to save CompanyForm : $companyFormDTO")
        if (companyFormDTO.id != null) {
            throw BadRequestAlertException(
                "A new companyForm cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = companyFormService.save(companyFormDTO)
        return ResponseEntity.created(URI("/api/company-forms/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    @PutMapping("/company-forms/{id}")
    fun updatecompanyForm(
        @PathVariable(value = "id", required = false) id: Long,
        @Valid @RequestBody companyFormDTO: CompanyFormDTO
    ): ResponseEntity<CompanyFormDTO> {
        log.debug("REST request to update CompanyForm : {}, {}", id, companyFormDTO)
        if (companyFormDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, companyFormDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!companyFormRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = companyFormService.update(companyFormDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    companyFormDTO.id.toString()
                )
            )
            .body(result)
    }

    @PatchMapping(value = ["/company-forms/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdatecompanyForm(
        @PathVariable(value = "id", required = false) id: Long,
        @NotNull @RequestBody companyFormDTO: CompanyFormDTO
    ): ResponseEntity<CompanyFormDTO> {
        log.debug("REST request to partial update CompanyForm partially : {}, {}", id, companyFormDTO)
        if (companyFormDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, companyFormDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!companyFormRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = companyFormService.partialUpdate(companyFormDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyFormDTO.id.toString())
        )
    }

    @GetMapping("/company-forms") fun getAllcompanyForms(
        criteria: CompanyFormCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<CompanyFormDTO>> {
        log.debug("REST request to get companyForms by criteria: $criteria")
        val page = companyFormQueryService.findByCriteria(criteria, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    @GetMapping("/company-forms/count")
    fun countcompanyForms(criteria: CompanyFormCriteria): ResponseEntity<Long> {
        log.debug("REST request to count companyForms by criteria: $criteria")
        return ResponseEntity.ok().body(companyFormQueryService.countByCriteria(criteria))
    }

    @GetMapping("/company-forms/{id}")
    fun getcompanyForm(@PathVariable id: Long): ResponseEntity<CompanyFormDTO> {
        log.debug("REST request to get CompanyForm : $id")
        val companyFormDTO = companyFormService.findOne(id)
        return ResponseUtil.wrapOrNotFound(companyFormDTO)
    }

    @DeleteMapping("/company-forms/{id}")
    fun deletecompanyForm(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete CompanyForm : $id")

        companyFormService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
