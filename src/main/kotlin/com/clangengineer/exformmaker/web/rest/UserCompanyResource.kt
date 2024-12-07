package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.repository.UserCompanyRepository
import com.clangengineer.exformmaker.service.UserCompanyQueryService
import com.clangengineer.exformmaker.service.UserCompanyService
import com.clangengineer.exformmaker.service.criteria.UserCompanyCriteria
import com.clangengineer.exformmaker.service.dto.UserCompanyDTO
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
    private val userCompanyService: UserCompanyService,
    private val userCompanyRepository: UserCompanyRepository,
    private val userCompanyQueryService: UserCompanyQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "userCompany"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PostMapping("/user-companys")
    fun createUserCompany(@Valid @RequestBody userCompanyDTO: UserCompanyDTO): ResponseEntity<UserCompanyDTO> {
        log.debug("REST request to save UserCompany : $userCompanyDTO")
        if (userCompanyDTO.id != null) {
            throw BadRequestAlertException(
                "A new userCompany cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = userCompanyService.save(userCompanyDTO)
        return ResponseEntity.created(URI("/api/user-companys/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    @PutMapping("/user-companys/{id}")
    fun updateUserCompany(
        @PathVariable(value = "id", required = false) id: Long,
        @Valid @RequestBody userCompanyDTO: UserCompanyDTO
    ): ResponseEntity<UserCompanyDTO> {
        log.debug("REST request to update UserCompany : {}, {}", id, userCompanyDTO)
        if (userCompanyDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, userCompanyDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!userCompanyRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = userCompanyService.update(userCompanyDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    userCompanyDTO.id.toString()
                )
            )
            .body(result)
    }

    @PatchMapping(value = ["/user-companys/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateUserCompany(
        @PathVariable(value = "id", required = false) id: Long,
        @NotNull @RequestBody userCompanyDTO: UserCompanyDTO
    ): ResponseEntity<UserCompanyDTO> {
        log.debug("REST request to partial update UserCompany partially : {}, {}", id, userCompanyDTO)
        if (userCompanyDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, userCompanyDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!userCompanyRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = userCompanyService.partialUpdate(userCompanyDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userCompanyDTO.id.toString())
        )
    }

    @GetMapping("/user-companys") fun getAllUserCompanys(
        criteria: UserCompanyCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<UserCompanyDTO>> {
        log.debug("REST request to get UserCompanys by criteria: $criteria")
        val page = userCompanyQueryService.findByCriteria(criteria, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    @GetMapping("/user-companys/count")
    fun countUserCompanys(criteria: UserCompanyCriteria): ResponseEntity<Long> {
        log.debug("REST request to count UserCompanys by criteria: $criteria")
        return ResponseEntity.ok().body(userCompanyQueryService.countByCriteria(criteria))
    }

    @GetMapping("/user-companys/{id}")
    fun getUserCompany(@PathVariable id: Long): ResponseEntity<UserCompanyDTO> {
        log.debug("REST request to get UserCompany : $id")
        val userCompanyDTO = userCompanyService.findOne(id)
        return ResponseUtil.wrapOrNotFound(userCompanyDTO)
    }

    @DeleteMapping("/user-companys/{id}")
    fun deleteUserCompany(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete UserCompany : $id")

        userCompanyService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
