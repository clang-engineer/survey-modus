package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.security.STAFF
import com.clangengineer.surveymodus.security.getCurrentUserLogin
import com.clangengineer.surveymodus.security.hasCurrentUserThisAuthority
import com.clangengineer.surveymodus.service.AuthorizedCompanyService
import com.clangengineer.surveymodus.service.CompanyQueryService
import com.clangengineer.surveymodus.service.CompanyService
import com.clangengineer.surveymodus.service.UserService
import com.clangengineer.surveymodus.service.criteria.CompanyCriteria
import com.clangengineer.surveymodus.service.dto.CompanyDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.jhipster.service.filter.LongFilter
import tech.jhipster.web.util.HeaderUtil
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class CompanyController(
    val companyService: CompanyService,
    val companyQueryService: CompanyQueryService,
    val authorizedCompanyService: AuthorizedCompanyService,
    val userService: UserService
) {
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

    @GetMapping("/companys/authorized")
    fun getAuthorizedCompanies(): ResponseEntity<List<CompanyDTO>> {
        log.debug("REST request to get authorized Companies")

        val result: List<CompanyDTO>

        if (hasCurrentUserThisAuthority(STAFF)) {
            val phone = getCurrentUserLogin().orElseThrow { RuntimeException("User not logged in") }
            result = authorizedCompanyService.fetchCompaniesByStaffPhone(phone)
        } else {
            val user = userService.getUserWithAuthorities().orElseThrow { RuntimeException("User not logged in") }
            result = companyQueryService.findByCriteria(
                CompanyCriteria(
                    userId = LongFilter().apply { equals = user.id }
                )
            )
        }

        return ResponseEntity.ok().body(result)
    }
}
