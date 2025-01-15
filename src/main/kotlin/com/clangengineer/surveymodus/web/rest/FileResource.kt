package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.repository.FileRepository
import com.clangengineer.surveymodus.service.FileQueryService
import com.clangengineer.surveymodus.service.FileService
import com.clangengineer.surveymodus.service.criteria.FileCriteria
import com.clangengineer.surveymodus.service.dto.FileDTO
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
class FileResource(
    private val fileService: FileService,
    private val fileRepository: FileRepository,
    private val fileQueryService: FileQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "file"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PostMapping("/files")
    fun createFile(@Valid @RequestBody fileDTO: FileDTO): ResponseEntity<FileDTO> {
        log.debug("REST request to save File : $fileDTO")
        if (fileDTO.id != null) {
            throw BadRequestAlertException(
                "A new file cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = fileService.save(fileDTO)
        return ResponseEntity.created(URI("/api/files/${result.id}"))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName, true, ENTITY_NAME, result.id.toString()
                )
            )
            .body(result)
    }

    @PutMapping("/files/{id}")
    fun updateFile(
        @PathVariable(value = "id", required = false) id: Long,
        @Valid @RequestBody fileDTO: FileDTO
    ): ResponseEntity<FileDTO> {
        log.debug("REST request to update File : {}, {}", id, fileDTO)
        if (fileDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, fileDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!fileRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = fileService.update(fileDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    fileDTO.id.toString()
                )
            )
            .body(result)
    }

    @PatchMapping(
        value = ["/files/{id}"],
        consumes = ["application/json", "application/merge-patch+json"]
    )
    @Throws(URISyntaxException::class)
    fun partialUpdateFile(
        @PathVariable(value = "id", required = false) id: Long,
        @NotNull @RequestBody fileDTO: FileDTO
    ): ResponseEntity<FileDTO> {
        log.debug("REST request to partial update File partially : {}, {}", id, fileDTO)
        if (fileDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, fileDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!fileRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = fileService.partialUpdate(fileDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, fileDTO.id.toString()
            )
        )
    }

    @GetMapping("/files")
    fun getAllFiles(
        criteria: FileCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<FileDTO>> {
        log.debug("REST request to get Files by criteria: $criteria")
        val page = fileQueryService.findByCriteria(criteria, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(),
            page
        )
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    @GetMapping("/files/count")
    fun countFiles(criteria: FileCriteria): ResponseEntity<Long> {
        log.debug("REST request to count Files by criteria: $criteria")
        return ResponseEntity.ok().body(fileQueryService.countByCriteria(criteria))
    }

    @GetMapping("/files/{id}")
    fun getFile(@PathVariable id: Long): ResponseEntity<FileDTO> {
        log.debug("REST request to get File : $id")
        val fileDTO = fileService.findOne(id)
        return ResponseUtil.wrapOrNotFound(fileDTO)
    }

    @DeleteMapping("/files/{id}")
    fun deleteFile(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete File : $id")

        fileService.delete(id)
        return ResponseEntity.noContent()
            .headers(
                HeaderUtil.createEntityDeletionAlert(
                    applicationName, true, ENTITY_NAME, id.toString()
                )
            ).build()
    }
}
