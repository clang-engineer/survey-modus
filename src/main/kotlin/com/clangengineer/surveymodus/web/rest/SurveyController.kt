package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.config.DOCUMENT_COMPANY_ID
import com.clangengineer.surveymodus.config.DOCUMENT_FORM_ID
import com.clangengineer.surveymodus.config.DOCUMENT_OBJECT_ID
import com.clangengineer.surveymodus.service.dto.SurveyDTO
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
class SurveyController {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    companion object {
        const val OBJECT_NAME = "survey"
    }

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @PostMapping("/surveys")
    fun createSurvey(@RequestParam collectionId: String, @RequestBody survey: SurveyDTO): ResponseEntity<SurveyDTO> {
        log.debug("REST request to save Datasource")

        val result = mongoTemplate.save(survey, collectionId) as SurveyDTO

        return ResponseEntity.created(URI("/api/surveys?collectionId=$collectionId"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, OBJECT_NAME, result.id))
            .body(result)
    }

    @GetMapping("/surveys")
    fun findAllSurvey(
        @RequestParam collectionId: String,
        @RequestParam companyId: Long,
        @RequestParam formId: Long
    ): ResponseEntity<List<SurveyDTO>> {
        log.debug("REST request to get all Surveys in collection : $collectionId for form : $formId")

        val ct1 = Criteria.where(DOCUMENT_COMPANY_ID).`is`(companyId)
        val ct2 = Criteria.where(DOCUMENT_FORM_ID).`is`(formId)

        val query = Query.query(Criteria().andOperator(ct1, ct2))

        val result = mongoTemplate.find(query, SurveyDTO::class.java, collectionId)

        return ResponseEntity.ok(result)
    }

    @GetMapping("/surveys/{surveyId}")
    fun findSurveyById(
        @PathVariable surveyId: String,
        @RequestParam collectionId: String,
    ): ResponseEntity<SurveyDTO> {
        log.debug("REST request to get Survey : $surveyId in collection : $collectionId")

        val query = Query.query(Criteria.where(DOCUMENT_OBJECT_ID).`is`(ObjectId(surveyId)))

        val result = mongoTemplate.findOne(query, SurveyDTO::class.java, collectionId)

        return ResponseEntity.ok(result)
    }

    @PutMapping("/surveys/{surveyId}")
    fun updateSurvey(
        @PathVariable surveyId: String,
        @RequestParam collectionId: String,
        @RequestBody survey: Map<String, Any>
    ): ResponseEntity<SurveyDTO> {
        log.debug("REST request to update Survey : $surveyId in collection : $collectionId")

        val query = Query.query(Criteria.where(DOCUMENT_OBJECT_ID).`is`(ObjectId(surveyId)))

        val update = Update()
        survey.filterKeys { it != DOCUMENT_OBJECT_ID }.forEach { key, value ->
            update.set(key, value)
        }

        val result = mongoTemplate.findAndModify(query, update, SurveyDTO::class.java, collectionId)

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, OBJECT_NAME, result.id))
            .body(result)
    }

    @DeleteMapping("/surveys/{surveyId}")
    fun deleteSurvey(@PathVariable surveyId: String, @RequestParam collectionId: String): ResponseEntity<Void> {
        log.debug("REST request to delete Survey : $surveyId in collection : $collectionId")

        val query = Query.query(Criteria.where(DOCUMENT_OBJECT_ID).`is`(ObjectId(surveyId)))

        mongoTemplate.remove(query, SurveyDTO::class.java, collectionId)

        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, OBJECT_NAME, surveyId))
            .build()
    }
}
