package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.config.DOCUMENT_FORM_ID
import com.clangengineer.surveymodus.service.FormService
import com.clangengineer.surveymodus.service.dto.DocumentDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class DocumentController(private val formService: FormService) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @PostMapping("/documents")
    fun createRow(@RequestBody document: DocumentDTO): ResponseEntity<Map<String, Any>> {
        log.debug("REST request to save Datasource")

        if (document.form == null || document.form?.category == null) {
            throw IllegalArgumentException("Form is required")
        }

        val result = mongoTemplate.save(document.toDocumentMap(), document.form?.category!!.id.toString())

        return ResponseEntity.created(null).body(result)
    }

    @GetMapping("/documents/forms/{formId}")
    fun findAllDocumentsByFormId(@PathVariable formId: String): ResponseEntity<List<Map<String, Any>>> {
        log.debug("REST request to get all Documents in Form : $formId")

        val category = formService.findOne(formId.toLong()).orElseThrow(
            { IllegalArgumentException("Form not found") }
        ).category ?: throw IllegalArgumentException("Category not found")

        val Query = Query()
        Query.addCriteria(Criteria.where(DOCUMENT_FORM_ID).`is`(formId))

        val result = mongoTemplate.find(Query, Map::class.java, category.id.toString()) as List<Map<String, Any>>

        return ResponseEntity.ok(result)
    }
}
