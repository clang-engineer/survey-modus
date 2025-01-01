package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.config.DOCUMENT_FORM_ID
import com.clangengineer.surveymodus.config.DOCUMENT_ID
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class DocumentController {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @PostMapping("/collections/{collectionId}")
    fun createDocument(@PathVariable collectionId: String, @RequestBody document: Map<String, Any>): ResponseEntity<Map<String, Any>> {
        log.debug("REST request to save Datasource")

        val result = mongoTemplate.save(document, collectionId) as Map<String, Any>

        return ResponseEntity.created(null).body(result)
    }

    @GetMapping("/collections/{collectionId}")
    fun findAllDocumentsInCollectionByFormId(@PathVariable collectionId: String, @RequestParam formId: String): ResponseEntity<List<Map<String, Any>>> {
        log.debug("REST request to get all Documents in collection : $collectionId for form : $formId")

        val Query = Query()
        Query.addCriteria(Criteria.where(DOCUMENT_FORM_ID).`is`(formId))

        val result = mongoTemplate.find(Query, Map::class.java, collectionId) as List<Map<String, Any>>

        return ResponseEntity.ok(result)
    }

    @GetMapping("/collections/{collectionId}/documents/{documentId}")
    fun findDocumentById(@PathVariable collectionId: String, @PathVariable documentId: String): ResponseEntity<Map<String, Any>> {
        log.debug("REST request to get Document : $documentId in collection : $collectionId")

        val Query = Query()
        Query.addCriteria(Criteria.where(DOCUMENT_ID).`is`(documentId))

        val result = mongoTemplate.findOne(Query, Map::class.java, collectionId) as Map<String, Any>

        return ResponseEntity.ok(result)
    }
}
