package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.service.MultipartFileService
import com.clangengineer.surveymodus.service.dto.FileDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import tech.jhipster.web.util.HeaderUtil
import java.net.URI

@RestController
@RequestMapping("/api")
class FileController(
    private val multipartFileService: MultipartFileService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "file"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PostMapping("/files/upload")
    fun uploadFile(@RequestBody file: MultipartFile): ResponseEntity<FileDTO> {
        log.debug("REST request to upload file")

        val result = multipartFileService.saveMultipartFile(file)

        return ResponseEntity.created(URI("/api/forms/updload?file=${file.originalFilename}"))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName, true,
                    FormResource.ENTITY_NAME, file.originalFilename
                )
            ).body(result)
    }
}
