package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.service.FileService
import com.clangengineer.surveymodus.service.MultipartFileService
import com.clangengineer.surveymodus.service.dto.FileDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.query.Param
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tech.jhipster.web.util.HeaderUtil
import java.net.URI
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api")
class FileController(
    private val multipartFileService: MultipartFileService,
    private val fileService: FileService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PostMapping("/files/upload")
    fun uploadFile(
        @RequestBody multipartFiles: List<MultipartFile>
    ): ResponseEntity<List<FileDTO>> {
        log.debug("REST request to upload file")

        val result = multipartFileService.createEntityAndSaveMultipartFiles(multipartFiles)

        return ResponseEntity.created(URI("/api/forms/updload"))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName, true,
                    FileResource.ENTITY_NAME, result.size.toString()
                )
            ).body(result)
    }

    @GetMapping("/files/download")
    fun downloadFile(
        @Param("fileId") fileId: Long,
        response: HttpServletResponse
    ): ResponseEntity<Unit> {
        log.debug("REST request to download file by id: $fileId")

        val fileEntity = fileService.findOne(fileId)
        val physicalFile = multipartFileService.getPhysicalFileOnDiskByFileEntityId(fileId)

        fileEntity.ifPresent { f ->
            response.contentType = f.type
        }

        response.setHeader("Content-Disposition", "attachment; filename=$fileId.pdf")
        response.outputStream.write(physicalFile)
        response.outputStream.flush()

        val header = HeaderUtil.createAlert(applicationName, "file.download", fileId.toString())

        return ResponseEntity.ok().headers(header).build()
    }
}
