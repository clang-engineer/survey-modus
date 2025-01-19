package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.config.SURVEY_COMPANY_ID
import com.clangengineer.surveymodus.config.SURVEY_FIELDS_PROPERTY
import com.clangengineer.surveymodus.config.SURVEY_FORM_ID
import com.clangengineer.surveymodus.config.SURVEY_OBJECT_ID
import com.clangengineer.surveymodus.service.criteria.SurveyCriteria
import com.clangengineer.surveymodus.service.dto.SurveyDTO
import com.clangengineer.surveymodus.web.rest.SurveyController.Companion.OBJECT_NAME
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SurveyService(val mongoTemplate: MongoTemplate) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun createSurvey(survey: SurveyDTO): SurveyDTO {
        log.debug("REST request to save Datasource")

        return mongoTemplate.save(survey, OBJECT_NAME) as SurveyDTO
    }

    fun findAll(criteria: SurveyCriteria): List<SurveyDTO> {
        log.debug("REST request to get all Surveys")

        val query = buildQueryFromCriteria(criteria)

        return mongoTemplate.find(query, SurveyDTO::class.java, OBJECT_NAME)
    }

    fun findOne(id: String): SurveyDTO {
        log.debug("REST request to get Survey : $id")

        val query = Query.query(Criteria.where(SURVEY_OBJECT_ID).`is`(ObjectId(id)))

        return mongoTemplate.findOne(query, SurveyDTO::class.java, OBJECT_NAME) ?: throw RuntimeException("Survey not found")
    }

    fun update(survey: SurveyDTO): SurveyDTO {
        log.debug("REST request to update Survey : $survey")

        val query = Query.query(Criteria.where(SURVEY_OBJECT_ID).`is`(ObjectId(survey.id)))

        val update = Update()

        update.set(SURVEY_COMPANY_ID, survey.companyId)
        update.set(SURVEY_FORM_ID, survey.formId)
        update.set(SURVEY_FIELDS_PROPERTY, survey.fields)

        return mongoTemplate.findAndModify(query, update, SurveyDTO::class.java, OBJECT_NAME)
    }

    fun delete(id: String) {
        log.debug("REST request to delete Survey : $id")

        val query = Query.query(Criteria.where(SURVEY_OBJECT_ID).`is`(ObjectId(id)))

        mongoTemplate.remove(query, OBJECT_NAME)
    }

    fun buildQueryFromCriteria(criteria: SurveyCriteria): Query {
        val queryCriteria = mutableListOf<Criteria>()

        criteria.companyId?.let {
            it.equals?.let { value -> queryCriteria.add(Criteria.where(SURVEY_COMPANY_ID).`is`(value)) }
        }

        criteria.formId?.let {
            it.equals?.let { value -> queryCriteria.add(Criteria.where(SURVEY_FORM_ID).`is`(value)) }
        }

        val query = if (queryCriteria.isNotEmpty()) {
            Query().addCriteria(Criteria().andOperator(*queryCriteria.toTypedArray()))
        } else {
            Query()
        }

        return query
    }
}
