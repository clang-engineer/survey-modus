package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.service.FormService
import com.clangengineer.surveymodus.service.dto.FormDTO
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
class FormController(val formService: FormService) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PutMapping("/forms/all")
    fun createAndUpdateAllForms(@Valid @RequestBody forms: List<FormDTO>): ResponseEntity<List<FormDTO>> {
        log.debug("REST request to update Forms : {}", forms)

        val result = formService.saveAll(forms)

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, FormResource.ENTITY_NAME, result.size.toString()))
            .body(result)
    }
}
