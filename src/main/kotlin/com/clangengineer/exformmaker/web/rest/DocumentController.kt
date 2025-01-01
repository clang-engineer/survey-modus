package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.service.dto.DocumentDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class DocumentController {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @PostMapping("/documents")
    fun createRow(@RequestBody document: DocumentDTO): ResponseEntity<Map<String, Any>> {
        log.debug("REST request to save Datasource")

        if (document.form == null || document.form?.category == null) {
            throw IllegalArgumentException("Form is required")
        }

        val result = mongoTemplate.save(document.row, document.form?.category!!.id.toString())

        return ResponseEntity.created(null).body(result)
    }
}
