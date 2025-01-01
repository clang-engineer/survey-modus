package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.service.FieldService
import com.clangengineer.surveymodus.service.dto.FieldDTO
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
class FieldController(val fieldService: FieldService) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PutMapping("/fields/all")
    fun createAndUpdateAllFields(@Valid @RequestBody fields: List<FieldDTO>): ResponseEntity<List<FieldDTO>> {
        log.debug("REST request to update Fields : {}", fields)

        val result = fieldService.saveAll(fields)

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, FieldResource.ENTITY_NAME, result.size.toString()))
            .body(result)
    }
}
