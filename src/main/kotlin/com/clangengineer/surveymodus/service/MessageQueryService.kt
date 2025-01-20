package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.config.SURVEY_COMPANY_ID
import com.clangengineer.surveymodus.service.criteria.MessageCriteria
import com.clangengineer.surveymodus.service.dto.MessageDTO
import com.clangengineer.surveymodus.web.rest.MessageController
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MessageQueryService(
    val mongoTemplate: MongoTemplate
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun findAll(criteria: MessageCriteria): List<MessageDTO> {
        log.debug("REST request to get all Messages")

        val query = buildQueryFromCriteria(criteria)

        return mongoTemplate.find(
            query,
            MessageDTO::class.java,
            MessageController.OBJECT_NAME
        )
    }

    fun buildQueryFromCriteria(criteria: MessageCriteria): Query {
        val queryCriteria = mutableListOf<Criteria>()

        criteria.companyId?.let {
            it.equals?.let { value ->
                queryCriteria.add(
                    Criteria.where(SURVEY_COMPANY_ID).`is`(value)
                )
            }
        }

        val query = if (queryCriteria.isNotEmpty()) {
            Query().addCriteria(Criteria().andOperator(*queryCriteria.toTypedArray()))
        } else {
            Query()
        }

        return query
    }
}
