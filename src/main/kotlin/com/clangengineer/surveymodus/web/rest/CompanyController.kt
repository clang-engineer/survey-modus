package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.service.CompanyService
import com.clangengineer.surveymodus.service.dto.CompanyDTO
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
class CompanyController(val companyService: CompanyService) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PutMapping("/companys/all")
    fun createAndUpdateAllCompanys(@Valid @RequestBody companys: List<CompanyDTO>): ResponseEntity<List<CompanyDTO>> {
        log.debug("REST request to update Companys : {}", companys)

        val result = companyService.saveAll(companys)

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, CompanyResource.ENTITY_NAME, result.size.toString()))
            .body(result)
    }
}
