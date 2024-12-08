package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.repository.GroupUserRepository
import com.clangengineer.exformmaker.service.GroupUserQueryService
import com.clangengineer.exformmaker.service.GroupUserService
import com.clangengineer.exformmaker.service.criteria.GroupUserCriteria
import com.clangengineer.exformmaker.service.dto.GroupUserDTO
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
class GroupUserResource(
    private val groupUserService: GroupUserService,
    private val groupUserRepository: GroupUserRepository,
    private val groupUserQueryService: GroupUserQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "groupUser"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PostMapping("/group-users")
    fun creategroupUser(@Valid @RequestBody groupUserDTO: GroupUserDTO): ResponseEntity<GroupUserDTO> {
        log.debug("REST request to save GroupUser : $groupUserDTO")
        if (groupUserDTO.id != null) {
            throw BadRequestAlertException(
                "A new groupUser cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = groupUserService.save(groupUserDTO)
        return ResponseEntity.created(URI("/api/group-users/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    @PutMapping("/group-users/{id}")
    fun updategroupUser(
        @PathVariable(value = "id", required = false) id: Long,
        @Valid @RequestBody groupUserDTO: GroupUserDTO
    ): ResponseEntity<GroupUserDTO> {
        log.debug("REST request to update GroupUser : {}, {}", id, groupUserDTO)
        if (groupUserDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, groupUserDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!groupUserRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = groupUserService.update(groupUserDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    groupUserDTO.id.toString()
                )
            )
            .body(result)
    }

    @PatchMapping(value = ["/group-users/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdategroupUser(
        @PathVariable(value = "id", required = false) id: Long,
        @NotNull @RequestBody groupUserDTO: GroupUserDTO
    ): ResponseEntity<GroupUserDTO> {
        log.debug("REST request to partial update GroupUser partially : {}, {}", id, groupUserDTO)
        if (groupUserDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, groupUserDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!groupUserRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = groupUserService.partialUpdate(groupUserDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupUserDTO.id.toString())
        )
    }

    @GetMapping("/group-users") fun getAllgroupUsers(
        criteria: GroupUserCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<GroupUserDTO>> {
        log.debug("REST request to get groupUsers by criteria: $criteria")
        val page = groupUserQueryService.findByCriteria(criteria, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    @GetMapping("/group-users/count")
    fun countgroupUsers(criteria: GroupUserCriteria): ResponseEntity<Long> {
        log.debug("REST request to count groupUsers by criteria: $criteria")
        return ResponseEntity.ok().body(groupUserQueryService.countByCriteria(criteria))
    }

    @GetMapping("/group-users/{id}")
    fun getgroupUser(@PathVariable id: Long): ResponseEntity<GroupUserDTO> {
        log.debug("REST request to get GroupUser : $id")
        val groupUserDTO = groupUserService.findOne(id)
        return ResponseUtil.wrapOrNotFound(groupUserDTO)
    }

    @DeleteMapping("/group-users/{id}")
    fun deletegroupUser(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete GroupUser : $id")

        groupUserService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
