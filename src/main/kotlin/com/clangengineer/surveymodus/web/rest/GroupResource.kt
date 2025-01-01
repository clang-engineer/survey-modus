package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.repository.GroupRepository
import com.clangengineer.surveymodus.service.GroupQueryService
import com.clangengineer.surveymodus.service.GroupService
import com.clangengineer.surveymodus.service.criteria.GroupCriteria
import com.clangengineer.surveymodus.service.dto.GroupDTO
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
class GroupResource(
    private val groupService: GroupService,
    private val groupRepository: GroupRepository,
    private val groupQueryService: GroupQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "group"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PostMapping("/groups")
    fun createGroup(@Valid @RequestBody groupDTO: GroupDTO): ResponseEntity<GroupDTO> {
        log.debug("REST request to save Group : $groupDTO")

        if (groupDTO.id != null) {
            throw BadRequestAlertException("A new group cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = groupService.save(groupDTO)
        return ResponseEntity.created(URI("/api/groups/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    @PutMapping("/groups/{id}")
    fun updateGroup(@PathVariable(value = "id", required = false) id: Long, @Valid @RequestBody groupDTO: GroupDTO): ResponseEntity<GroupDTO> {
        log.debug("REST request to update Group : {}, {}", id, groupDTO)

        if (groupDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, groupDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!groupRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = groupService.update(groupDTO)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupDTO.id.toString()))
            .body(result)
    }

    @PatchMapping(value = ["/groups/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateGroup(@PathVariable(value = "id", required = false) id: Long, @NotNull @RequestBody groupDTO: GroupDTO): ResponseEntity<GroupDTO> {
        log.debug("REST request to partial update Group partially : {}, {}", id, groupDTO)

        if (groupDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, groupDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!groupRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = groupService.partialUpdate(groupDTO)

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupDTO.id.toString()))
    }

    @GetMapping("/groups")
    fun getAllGroups(criteria: GroupCriteria, @org.springdoc.api.annotations.ParameterObject pageable: Pageable): ResponseEntity<MutableList<GroupDTO>> {
        log.debug("REST request to get Groups by criteria: $criteria")

        val page = groupQueryService.findByCriteria(criteria, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    @GetMapping("/groups/count")
    fun countGroups(criteria: GroupCriteria): ResponseEntity<Long> {
        log.debug("REST request to count Groups by criteria: $criteria")
        return ResponseEntity.ok().body(groupQueryService.countByCriteria(criteria))
    }

    @GetMapping("/groups/{id}")
    fun getGroup(@PathVariable id: Long): ResponseEntity<GroupDTO> {
        log.debug("REST request to get Group : $id")

        val groupDTO = groupService.findOne(id)
        return ResponseUtil.wrapOrNotFound(groupDTO)
    }

    @DeleteMapping("/groups/{id}")
    fun deleteGroup(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Group : $id")

        groupService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
