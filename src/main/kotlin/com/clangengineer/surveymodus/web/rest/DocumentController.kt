package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.config.DOCUMENT_COMPANY_ID
import com.clangengineer.surveymodus.config.DOCUMENT_FORM_ID
import com.clangengineer.surveymodus.config.DOCUMENT_ID
import com.clangengineer.surveymodus.config.DOCUMENT_OBJECT_ID
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.jhipster.web.util.HeaderUtil
import java.net.URI

@RestController
@RequestMapping("/api")
class DocumentController {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    companion object {
        const val OBJECT_NAME = "document"
    }

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @PostMapping("/collections/{collectionId}/documents")
    fun createDocument(@PathVariable collectionId: String, @RequestBody document: Map<String, Any>): ResponseEntity<Map<String, Any>> {
        log.debug("REST request to save Datasource")

        val result = mongoTemplate.save(document, collectionId) as Map<String, Any>
        val serialized = serializeDocument(result)

        return ResponseEntity.created(URI("/api/collections/$collectionId/documents/${serialized[DOCUMENT_ID]}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, OBJECT_NAME, serialized[DOCUMENT_ID].toString()))
            .body(result)
    }

    @GetMapping("/collections/{collectionId}/documents")
    fun findAllDocumentsInCollectionByFormId(
        @PathVariable collectionId: String,
        @RequestParam companyId: Long,
        @RequestParam formId: Long
    ): ResponseEntity<List<Map<String, Any>>> {
        log.debug("REST request to get all Documents in collection : $collectionId for form : $formId")

        val ct1 = Criteria.where(DOCUMENT_COMPANY_ID).`is`(companyId)
        val ct2 = Criteria.where(DOCUMENT_FORM_ID).`is`(formId)

        val query = Query()
        query.addCriteria(Criteria().andOperator(ct1, ct2))

        val result = mongoTemplate.find(query, Map::class.java, collectionId) as List<Map<String, Any>>

        val serialized = serializeDocuments(result)

        return ResponseEntity.ok(serialized)
    }

    @GetMapping("/collections/{collectionId}/documents/{documentId}")
    fun findDocumentById(@PathVariable collectionId: String, @PathVariable documentId: String): ResponseEntity<Map<String, Any>> {
        log.debug("REST request to get Document : $documentId in collection : $collectionId")

        val query = Query()
        query.addCriteria(Criteria.where(DOCUMENT_OBJECT_ID).`is`(ObjectId(documentId)))

        val result = mongoTemplate.findOne(query, Map::class.java, collectionId) as Map<String, Any>

        val serialized = serializeDocument(result)

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
        query.addCriteria(Criteria.where(DOCUMENT_OBJECT_ID).`is`(ObjectId(documentId)))

        val update = Update()
        document.filterKeys { it != DOCUMENT_OBJECT_ID }.forEach { key, value ->
            update.set(key, value)
        }

        val result = mongoTemplate.findAndModify(query, update, Map::class.java, collectionId) as Map<String, Any>
        val serialized = serializeDocument(result)

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, OBJECT_NAME, serialized[DOCUMENT_ID].toString()))
            .body(result)
    }

    @DeleteMapping("/collections/{collectionId}/documents/{documentId}")
    fun deleteDocument(@PathVariable collectionId: String, @PathVariable documentId: String): ResponseEntity<Void> {
        log.debug("REST request to delete Document : $documentId in collection : $collectionId")

        val query = Query()
        query.addCriteria(Criteria.where(DOCUMENT_OBJECT_ID).`is`(ObjectId(documentId)))

        mongoTemplate.remove(query, Map::class.java, collectionId)

        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, OBJECT_NAME, documentId))
            .build()
    }
}

fun serializeDocument(document: Map<String, Any>): Map<String, Any> {
    return document.toMutableMap().apply {
        val objectId = this[DOCUMENT_OBJECT_ID] as ObjectId
        this[DOCUMENT_ID] = objectId.toHexString()
    }
}

fun serializeDocuments(documents: List<Map<String, Any>>): List<Map<String, Any>> {
    return documents.map { serializeDocument(it) }
}
