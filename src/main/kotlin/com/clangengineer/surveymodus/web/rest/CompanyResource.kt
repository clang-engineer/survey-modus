package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.repository.CompanyRepository
import com.clangengineer.surveymodus.service.CompanyQueryService
import com.clangengineer.surveymodus.service.CompanyService
import com.clangengineer.surveymodus.service.criteria.CompanyCriteria
import com.clangengineer.surveymodus.service.dto.CompanyDTO
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
class CompanyResource(
    private val companyService: CompanyService,
    private val companyRepository: CompanyRepository,
    private val companyQueryService: CompanyQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "company"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PostMapping("/companys")
    fun createCompany(@Valid @RequestBody companyDTO: CompanyDTO): ResponseEntity<CompanyDTO> {
        log.debug("REST request to save Company : $companyDTO")

        if (companyDTO.id != null) {
            throw BadRequestAlertException("A new company cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = companyService.save(companyDTO)
        return ResponseEntity.created(URI("/api/companys/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    @PutMapping("/companys/{id}")
    fun updateCompany(@PathVariable(value = "id", required = false) id: Long, @Valid @RequestBody companyDTO: CompanyDTO): ResponseEntity<CompanyDTO> {
        log.debug("REST request to update Company : {}, {}", id, companyDTO)

        if (companyDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, companyDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!companyRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = companyService.update(companyDTO)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyDTO.id.toString()))
            .body(result)
    }

    @PatchMapping(value = ["/companys/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateCompany(@PathVariable(value = "id", required = false) id: Long, @NotNull @RequestBody companyDTO: CompanyDTO): ResponseEntity<CompanyDTO> {
        log.debug("REST request to partial update Company partially : {}, {}", id, companyDTO)

        if (companyDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, companyDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!companyRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = companyService.partialUpdate(companyDTO)

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyDTO.id.toString()))
    }

    @GetMapping("/companys")
    fun getAllCompanys(criteria: CompanyCriteria, @org.springdoc.api.annotations.ParameterObject pageable: Pageable): ResponseEntity<MutableList<CompanyDTO>> {
        log.debug("REST request to get Companys by criteria: $criteria")

        val page = companyQueryService.findByCriteria(criteria, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    @GetMapping("/companys/count")
    fun countCompanys(criteria: CompanyCriteria): ResponseEntity<Long> {
        log.debug("REST request to count Companys by criteria: $criteria")
        return ResponseEntity.ok().body(companyQueryService.countByCriteria(criteria))
    }

    @GetMapping("/companys/{id}")
    fun getCompany(@PathVariable id: Long): ResponseEntity<CompanyDTO> {
        log.debug("REST request to get Company : $id")

        val companyDTO = companyService.findOne(id)
        return ResponseUtil.wrapOrNotFound(companyDTO)
    }

    @DeleteMapping("/companys/{id}")
    fun deleteCompany(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Company : $id")

        companyService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
