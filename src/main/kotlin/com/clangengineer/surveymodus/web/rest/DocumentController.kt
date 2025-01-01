package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.config.DOCUMENT_FORM_ID
import com.clangengineer.surveymodus.config.DOCUMENT_ID
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class DocumentController {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @PostMapping("/collections/{collectionId}/documents")
    fun createDocument(@PathVariable collectionId: String, @RequestBody document: Map<String, Any>): ResponseEntity<Map<String, Any>> {
        log.debug("REST request to save Datasource")

        val result = mongoTemplate.save(document, collectionId) as Map<String, Any>

        return ResponseEntity.created(null).body(result)
    }

    @GetMapping("/collections/{collectionId}/documents")
    fun findAllDocumentsInCollectionByFormId(@PathVariable collectionId: String, @RequestParam formId: String): ResponseEntity<List<Map<String, Any>>> {
        log.debug("REST request to get all Documents in collection : $collectionId for form : $formId")

        val query = Query()
        query.addCriteria(Criteria.where(DOCUMENT_FORM_ID).`is`(formId))

        val result = mongoTemplate.find(query, Map::class.java, collectionId) as List<Map<String, Any>>

        return ResponseEntity.ok(result)
    }

    @GetMapping("/collections/{collectionId}/documents/{documentId}")
    fun findDocumentById(@PathVariable collectionId: String, @PathVariable documentId: String): ResponseEntity<Map<String, Any>> {
        log.debug("REST request to get Document : $documentId in collection : $collectionId")

        val query = Query()
        query.addCriteria(Criteria.where(DOCUMENT_ID).`is`(ObjectId(documentId)))

        val result = mongoTemplate.findOne(query, Map::class.java, collectionId) as Map<String, Any>

        val serialized = result.toMutableMap().apply {
            val objectId = this[DOCUMENT_ID]
            if (objectId is ObjectId) {
                this[DOCUMENT_ID] = objectId.toString()
            }
        }

        return ResponseEntity.ok(serialized)
    }

    @PutMapping("/collections/{collectionId}/documents/{documentId}")
    fun updateDocument(
        @PathVariable collectionId: String,
        @PathVariable documentId: String,
        @RequestBody document: Map<String, Any>
    ): ResponseEntity<Map<String, Any>> {
        log.debug("REST request to update Document : $documentId in collection : $collectionId")

        val query = Query()
        query.addCriteria(Criteria.where(DOCUMENT_ID).`is`(ObjectId(documentId)))

        val update = Update()
        document.filterKeys { it != DOCUMENT_ID }.forEach { key, value ->
            update.set(key, value)
        }

        val result = mongoTemplate.findAndModify(query, update, Map::class.java, collectionId) as Map<String, Any>

        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/collections/{collectionId}/documents/{documentId}")
    fun deleteDocument(@PathVariable collectionId: String, @PathVariable documentId: String): ResponseEntity<Void> {
        log.debug("REST request to delete Document : $documentId in collection : $collectionId")

        val query = Query()
        query.addCriteria(Criteria.where(DOCUMENT_ID).`is`(ObjectId(documentId)))

        mongoTemplate.remove(query, Map::class.java, collectionId)

        return ResponseEntity.noContent().build()
    }
}
