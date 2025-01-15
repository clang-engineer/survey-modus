package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.service.GroupService
import com.clangengineer.surveymodus.service.dto.GroupDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.jhipster.web.util.HeaderUtil
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class GroupController(val groupService: GroupService) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PutMapping("/groups/all")
    fun createAndUpdateAllGroups(@Valid @RequestBody groups: List<GroupDTO>): ResponseEntity<List<GroupDTO>> {
        log.debug("REST request to update Groups : {}", groups)

        val result = groupService.saveAll(groups)

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, GroupResource.ENTITY_NAME, result.size.toString()))
            .body(result)
    }
}
