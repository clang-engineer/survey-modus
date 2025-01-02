package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.config.DOCUMENT_COMPANY_ID
import com.clangengineer.surveymodus.config.DOCUMENT_FORM_ID
import com.clangengineer.surveymodus.config.DOCUMENT_OBJECT_ID
import com.clangengineer.surveymodus.service.dto.DocumentDTO
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
    fun createDocument(@PathVariable collectionId: String, @RequestBody document: DocumentDTO): ResponseEntity<DocumentDTO> {
        log.debug("REST request to save Datasource")

        val result = mongoTemplate.save(document, collectionId) as DocumentDTO

        return ResponseEntity.created(URI("/api/collections/$collectionId/documents/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, OBJECT_NAME, result.id))
            .body(result)
    }

    @GetMapping("/collections/{collectionId}/documents")
    fun findAllDocumentsInCollectionByFormId(
        @PathVariable collectionId: String,
        @RequestParam companyId: Long,
        @RequestParam formId: Long
    ): ResponseEntity<List<DocumentDTO>> {
        log.debug("REST request to get all Documents in collection : $collectionId for form : $formId")

        val ct1 = Criteria.where(DOCUMENT_COMPANY_ID).`is`(companyId)
        val ct2 = Criteria.where(DOCUMENT_FORM_ID).`is`(formId)

        val query = Query.query(Criteria().andOperator(ct1, ct2))

        val result = mongoTemplate.find(query, DocumentDTO::class.java, collectionId)

        return ResponseEntity.ok(result)
    }

    @GetMapping("/collections/{collectionId}/documents/{documentId}")
    fun findDocumentById(@PathVariable collectionId: String, @PathVariable documentId: String): ResponseEntity<DocumentDTO> {
        log.debug("REST request to get Document : $documentId in collection : $collectionId")

        val query = Query.query(Criteria.where(DOCUMENT_OBJECT_ID).`is`(ObjectId(documentId)))

        val result = mongoTemplate.findOne(query, DocumentDTO::class.java, collectionId)

        return ResponseEntity.ok(result)
    }

    @PutMapping("/collections/{collectionId}/documents/{documentId}")
    fun updateDocument(
        @PathVariable collectionId: String,
        @PathVariable documentId: String,
        @RequestBody document: Map<String, Any>
    ): ResponseEntity<DocumentDTO> {
        log.debug("REST request to update Document : $documentId in collection : $collectionId")

        val query = Query.query(Criteria.where(DOCUMENT_OBJECT_ID).`is`(ObjectId(documentId)))

        val update = Update()
        document.filterKeys { it != DOCUMENT_OBJECT_ID }.forEach { key, value ->
            update.set(key, value)
        }

        val result = mongoTemplate.findAndModify(query, update, DocumentDTO::class.java, collectionId)

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, OBJECT_NAME, result.id))
            .body(result)
    }

    @DeleteMapping("/collections/{collectionId}/documents/{documentId}")
    fun deleteDocument(@PathVariable collectionId: String, @PathVariable documentId: String): ResponseEntity<Void> {
        log.debug("REST request to delete Document : $documentId in collection : $collectionId")

        val query = Query.query(Criteria.where(DOCUMENT_OBJECT_ID).`is`(ObjectId(documentId)))

        mongoTemplate.remove(query, DocumentDTO::class.java, collectionId)

        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, OBJECT_NAME, documentId))
            .build()
    }
}
