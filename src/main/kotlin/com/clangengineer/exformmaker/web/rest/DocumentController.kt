package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.service.dto.DocumentDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class DocumentController {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @PostMapping("/documents")
    fun createRow(@RequestBody document: DocumentDTO): ResponseEntity<String> {
        log.debug("REST request to save Datasource")

        mongoTemplate.save(document.row, document.form?.category!!.id.toString())

        return ResponseEntity.ok("hi")
    }
}
