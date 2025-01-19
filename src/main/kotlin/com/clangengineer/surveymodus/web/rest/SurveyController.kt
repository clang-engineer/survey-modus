package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.service.SurveyService
import com.clangengineer.surveymodus.service.criteria.SurveyCriteria
import com.clangengineer.surveymodus.service.dto.SurveyDTO
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.jhipster.web.util.HeaderUtil
import java.net.URI

@RestController
@RequestMapping("/api")
class SurveyController(
    private val surveyService: SurveyService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    companion object {
        const val OBJECT_NAME = "survey"
    }

    @PostMapping("/surveys")
    fun createSurvey(@RequestBody survey: SurveyDTO): ResponseEntity<SurveyDTO> {
        log.debug("REST request to save Datasource")

        val result = surveyService.createSurvey(survey)

        return ResponseEntity.created(URI("/api/surveys/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, OBJECT_NAME, result.id))
            .body(result)
    }

    @GetMapping("/surveys")
    fun findAllSurvey(criteria: SurveyCriteria): ResponseEntity<List<SurveyDTO>> {
        log.debug("REST request to get all Surveys in collection by criteria : $criteria")

        val result = surveyService.findAll(criteria)

        return ResponseEntity.ok(result)
    }

    @GetMapping("/surveys/{surveyId}")
    fun findSurveyById(@PathVariable surveyId: String): ResponseEntity<SurveyDTO> {
        log.debug("REST request to get Survey : $surveyId")

        val result = surveyService.findOne(surveyId)

        return ResponseEntity.ok(result)
    }

    @PutMapping("/surveys/{surveyId}")
    fun updateSurvey(@PathVariable surveyId: String, @RequestBody survey: SurveyDTO): ResponseEntity<SurveyDTO> {
        log.debug("REST request to update Survey : $surveyId")

        survey._id = ObjectId(surveyId)
        val result = surveyService.update(survey)

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, OBJECT_NAME, result.id))
            .body(result)
    }

    @DeleteMapping("/surveys/{surveyId}")
    fun deleteSurvey(@PathVariable surveyId: String): ResponseEntity<Void> {
        log.debug("REST request to delete Survey : $surveyId")

        surveyService.delete(surveyId)

        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, OBJECT_NAME, surveyId))
            .build()
    }
}
