package com.clangengineer.exformmaker.web.rest

import com.clangengineer.exformmaker.repository.UserPointRepository
import com.clangengineer.exformmaker.service.UserPointQueryService
import com.clangengineer.exformmaker.service.UserPointService
import com.clangengineer.exformmaker.service.criteria.UserPointCriteria
import com.clangengineer.exformmaker.service.dto.UserPointDTO
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
import java.util.Objects
import javax.validation.Valid
import javax.validation.constraints.NotNull

private const val ENTITY_NAME = "userPoint"
/**
 * REST controller for managing [com.clangengineer.exformmaker.domain.UserPoint].
 */
@RestController
@RequestMapping("/api")
class UserPointResource(
    private val userPointService: UserPointService,
    private val userPointRepository: UserPointRepository,
    private val userPointQueryService: UserPointQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "userPoint"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /user-points` : Create a new userPoint.
     *
     * @param userPointDTO the userPointDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new userPointDTO, or with status `400 (Bad Request)` if the userPoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-points")
    fun createUserPoint(@Valid @RequestBody userPointDTO: UserPointDTO): ResponseEntity<UserPointDTO> {
        log.debug("REST request to save UserPoint : $userPointDTO")
        if (userPointDTO.id != null) {
            throw BadRequestAlertException(
                "A new userPoint cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = userPointService.save(userPointDTO)
        return ResponseEntity.created(URI("/api/user-points/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /user-points/:id} : Updates an existing userPoint.
     *
     * @param id the id of the userPointDTO to save.
     * @param userPointDTO the userPointDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated userPointDTO,
     * or with status `400 (Bad Request)` if the userPointDTO is not valid,
     * or with status `500 (Internal Server Error)` if the userPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-points/{id}")
    fun updateUserPoint(
        @PathVariable(value = "id", required = false) id: Long,
        @Valid @RequestBody userPointDTO: UserPointDTO
    ): ResponseEntity<UserPointDTO> {
        log.debug("REST request to update UserPoint : {}, {}", id, userPointDTO)
        if (userPointDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, userPointDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!userPointRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = userPointService.update(userPointDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    userPointDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /user-points/:id} : Partial updates given fields of an existing userPoint, field will ignore if it is null
     *
     * @param id the id of the userPointDTO to save.
     * @param userPointDTO the userPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPointDTO,
     * or with status {@code 400 (Bad Request)} if the userPointDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userPointDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/user-points/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateUserPoint(
        @PathVariable(value = "id", required = false) id: Long,
        @NotNull @RequestBody userPointDTO: UserPointDTO
    ): ResponseEntity<UserPointDTO> {
        log.debug("REST request to partial update UserPoint partially : {}, {}", id, userPointDTO)
        if (userPointDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, userPointDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!userPointRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = userPointService.partialUpdate(userPointDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPointDTO.id.toString())
        )
    }

    /**
     * `GET  /user-points` : get all the userPoints.
     *
     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of userPoints in body.
     */
    @GetMapping("/user-points") fun getAllUserPoints(
        criteria: UserPointCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<UserPointDTO>> {
        log.debug("REST request to get UserPoints by criteria: $criteria")
        val page = userPointQueryService.findByCriteria(criteria, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /user-points/count}` : count all the userPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the count in body.
     */
    @GetMapping("/user-points/count")
    fun countUserPoints(criteria: UserPointCriteria): ResponseEntity<Long> {
        log.debug("REST request to count UserPoints by criteria: $criteria")
        return ResponseEntity.ok().body(userPointQueryService.countByCriteria(criteria))
    }

    /**
     * `GET  /user-points/:id` : get the "id" userPoint.
     *
     * @param id the id of the userPointDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the userPointDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/user-points/{id}")
    fun getUserPoint(@PathVariable id: Long): ResponseEntity<UserPointDTO> {
        log.debug("REST request to get UserPoint : $id")
        val userPointDTO = userPointService.findOne(id)
        return ResponseUtil.wrapOrNotFound(userPointDTO)
    }
    /**
     *  `DELETE  /user-points/:id` : delete the "id" userPoint.
     *
     * @param id the id of the userPointDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/user-points/{id}")
    fun deleteUserPoint(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete UserPoint : $id")

        userPointService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
