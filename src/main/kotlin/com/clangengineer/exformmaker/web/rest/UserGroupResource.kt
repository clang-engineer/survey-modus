package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.repository.UserGroupRepository
import com.clangengineer.exformmaker.service.UserGroupQueryService
import com.clangengineer.exformmaker.service.UserGroupService
import com.clangengineer.exformmaker.service.criteria.UserGroupCriteria
import com.clangengineer.exformmaker.service.dto.UserGroupDTO
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
class UserGroupResource(
    private val userGroupService: UserGroupService,
    private val userGroupRepository: UserGroupRepository,
    private val userGroupQueryService: UserGroupQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "userGroup"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PostMapping("/user-groups")
    fun createUserGroup(@Valid @RequestBody userGroupDTO: UserGroupDTO): ResponseEntity<UserGroupDTO> {
        log.debug("REST request to save UserGroup : $userGroupDTO")
        if (userGroupDTO.id != null) {
            throw BadRequestAlertException(
                "A new userGroup cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = userGroupService.save(userGroupDTO)
        return ResponseEntity.created(URI("/api/user-groups/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    @PutMapping("/user-groups/{id}")
    fun updateUserGroup(
        @PathVariable(value = "id", required = false) id: Long,
        @Valid @RequestBody userGroupDTO: UserGroupDTO
    ): ResponseEntity<UserGroupDTO> {
        log.debug("REST request to update UserGroup : {}, {}", id, userGroupDTO)
        if (userGroupDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, userGroupDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!userGroupRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = userGroupService.update(userGroupDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    userGroupDTO.id.toString()
                )
            )
            .body(result)
    }

    @PatchMapping(value = ["/user-groups/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateUserGroup(
        @PathVariable(value = "id", required = false) id: Long,
        @NotNull @RequestBody userGroupDTO: UserGroupDTO
    ): ResponseEntity<UserGroupDTO> {
        log.debug("REST request to partial update UserGroup partially : {}, {}", id, userGroupDTO)
        if (userGroupDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, userGroupDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!userGroupRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = userGroupService.partialUpdate(userGroupDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userGroupDTO.id.toString())
        )
    }

    @GetMapping("/user-groups") fun getAllUserGroups(
        criteria: UserGroupCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<UserGroupDTO>> {
        log.debug("REST request to get UserGroups by criteria: $criteria")
        val page = userGroupQueryService.findByCriteria(criteria, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    @GetMapping("/user-groups/count")
    fun countUserGroups(criteria: UserGroupCriteria): ResponseEntity<Long> {
        log.debug("REST request to count UserGroups by criteria: $criteria")
        return ResponseEntity.ok().body(userGroupQueryService.countByCriteria(criteria))
    }

    @GetMapping("/user-groups/{id}")
    fun getUserGroup(@PathVariable id: Long): ResponseEntity<UserGroupDTO> {
        log.debug("REST request to get UserGroup : $id")
        val userGroupDTO = userGroupService.findOne(id)
        return ResponseUtil.wrapOrNotFound(userGroupDTO)
    }

    @DeleteMapping("/user-groups/{id}")
    fun deleteUserGroup(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete UserGroup : $id")

        userGroupService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
