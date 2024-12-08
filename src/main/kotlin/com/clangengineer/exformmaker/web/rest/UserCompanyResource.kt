package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.repository.GroupCompanyRepository
import com.clangengineer.exformmaker.service.GroupCompanyQueryService
import com.clangengineer.exformmaker.service.GroupCompanyService
import com.clangengineer.exformmaker.service.criteria.GroupCompanyCriteria
import com.clangengineer.exformmaker.service.dto.GroupCompanyDTO
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
class UserCompanyResource(
    private val groupCompanyService: GroupCompanyService,
    private val groupCompanyRepository: GroupCompanyRepository,
    private val groupCompanyQueryService: GroupCompanyQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "userCompany"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PostMapping("/group-companys")
    fun createUserCompany(@Valid @RequestBody groupCompanyDTO: GroupCompanyDTO): ResponseEntity<GroupCompanyDTO> {
        log.debug("REST request to save GroupCompany : $groupCompanyDTO")
        if (groupCompanyDTO.id != null) {
            throw BadRequestAlertException(
                "A new userCompany cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = groupCompanyService.save(groupCompanyDTO)
        return ResponseEntity.created(URI("/api/group-companys/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    @PutMapping("/group-companys/{id}")
    fun updateUserCompany(
        @PathVariable(value = "id", required = false) id: Long,
        @Valid @RequestBody groupCompanyDTO: GroupCompanyDTO
    ): ResponseEntity<GroupCompanyDTO> {
        log.debug("REST request to update GroupCompany : {}, {}", id, groupCompanyDTO)
        if (groupCompanyDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, groupCompanyDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!groupCompanyRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = groupCompanyService.update(groupCompanyDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    groupCompanyDTO.id.toString()
                )
            )
            .body(result)
    }

    @PatchMapping(value = ["/group-companys/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateUserCompany(
        @PathVariable(value = "id", required = false) id: Long,
        @NotNull @RequestBody groupCompanyDTO: GroupCompanyDTO
    ): ResponseEntity<GroupCompanyDTO> {
        log.debug("REST request to partial update GroupCompany partially : {}, {}", id, groupCompanyDTO)
        if (groupCompanyDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, groupCompanyDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!groupCompanyRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = groupCompanyService.partialUpdate(groupCompanyDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupCompanyDTO.id.toString())
        )
    }

    @GetMapping("/group-companys") fun getAllUserCompanys(
        criteria: GroupCompanyCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<GroupCompanyDTO>> {
        log.debug("REST request to get UserCompanys by criteria: $criteria")
        val page = groupCompanyQueryService.findByCriteria(criteria, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    @GetMapping("/group-companys/count")
    fun countUserCompanys(criteria: GroupCompanyCriteria): ResponseEntity<Long> {
        log.debug("REST request to count UserCompanys by criteria: $criteria")
        return ResponseEntity.ok().body(groupCompanyQueryService.countByCriteria(criteria))
    }

    @GetMapping("/group-companys/{id}")
    fun getUserCompany(@PathVariable id: Long): ResponseEntity<GroupCompanyDTO> {
        log.debug("REST request to get GroupCompany : $id")
        val userCompanyDTO = groupCompanyService.findOne(id)
        return ResponseUtil.wrapOrNotFound(userCompanyDTO)
    }

    @DeleteMapping("/group-companys/{id}")
    fun deleteUserCompany(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete GroupCompany : $id")

        groupCompanyService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
