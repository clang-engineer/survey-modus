package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.config.SURVEY_COMPANY_ID
import com.clangengineer.surveymodus.config.SURVEY_FIELDS_PROPERTY
import com.clangengineer.surveymodus.config.SURVEY_FORM_ID
import com.clangengineer.surveymodus.domain.Field
import com.clangengineer.surveymodus.domain.embeddable.FieldAttribute
import com.clangengineer.surveymodus.domain.enumeration.type
import com.clangengineer.surveymodus.service.dto.CompanyDTO
import com.clangengineer.surveymodus.service.dto.FieldDTO
import com.clangengineer.surveymodus.service.dto.FormDTO
import com.clangengineer.surveymodus.service.dto.SurveyDTO
import com.clangengineer.surveymodus.service.mapper.CompanyMapper
import com.clangengineer.surveymodus.service.mapper.FieldMapper
import com.clangengineer.surveymodus.service.mapper.FormMapper
import org.assertj.core.api.Assertions.*
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SurveyControllerIT {
    @Autowired
    private lateinit var datasourceMockMvc: MockMvc

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var formMapper: FormMapper

    @Autowired
    private lateinit var fieldMapper: FieldMapper

    @Autowired
    private lateinit var companyMapper: CompanyMapper

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    private lateinit var company: CompanyDTO

    private lateinit var form: FormDTO

    private val fieldList = mutableListOf<FieldDTO>()

    @BeforeEach
    fun init() {
        val form = FormResourceIT.createEntity(em)
        em.persist(form)
        em.flush()
        this.form = formMapper.toDto(form)

        val fieldList = mutableListOf<Field>()
        for (i in 1..5) {
            val field = Field(title = "title $i", description = "description $i", activated = true)
            field.attribute = FieldAttribute(type = type.TEXT)
            field.form = form
            em.persist(field)
            em.flush()

            fieldList += field
        }

        this.fieldList.addAll(fieldList.map { fieldMapper.toDto(it) })

        val company = CompanyResourceIT.createEntity(em)
        em.persist(company)
        em.flush()
        this.company = companyMapper.toDto(company)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun testCreateDocumentInCollection() {
        val fieldMapArray = mutableListOf<Map<String, Any>>()
        fieldList.forEach {
            fieldMapArray.add(
                mapOf("key" to it.id.toString(), "value" to Math.random())
            )
        }

        val survey =
            SurveyDTO(companyId = company.id, formId = form.id, fields = fieldMapArray)

        datasourceMockMvc.perform(
            post("/api/surveys?collectionId=${form.category!!.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(survey))
        ).andExpect(status().isCreated())

        val result = mongoTemplate.findAll(Map::class.java, form.category!!.id.toString())
        assertThat(result).hasSize(1)
        assertThat(result[0][SURVEY_COMPANY_ID]).isEqualTo(company.id)
        assertThat(result[0][SURVEY_FORM_ID]).isEqualTo(form.id)
        assertThat(result[0][SURVEY_FIELDS_PROPERTY]).isEqualTo(fieldMapArray)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun testFindAllDocumentsInCollectionsByFormId() {
        for (i in 1..5) {
            val fieldMapArray = mutableListOf<Map<String, Any>>()
            fieldList.forEach {
                fieldMapArray.add(
                    mapOf(
                        "key" to it.id.toString(),
                        "value" to Math.random()
                    )
                )
            }

            val survey = SurveyDTO(companyId = company.id, formId = form.id, fields = fieldMapArray)
            mongoTemplate.save(survey, form.category!!.id.toString())
        }

        val filterString = "companyId.equals=${company.id}&formId.equals=${form.id}"

        val API_URI = "/api/surveys?collectionId=${form.category!!.id}&$filterString"
        datasourceMockMvc.perform(get(API_URI))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").value(Matchers.hasSize<Any>(5)))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun testFindOneDocumentByDocumentId() {
        val fieldMapArray = mutableListOf<Map<String, Any>>()
        fieldList.forEach {
            fieldMapArray.add(
                mapOf("key" to it.id.toString(), "value" to Math.random())
            )
        }

        val survey =
            SurveyDTO(companyId = company.id, formId = form.id, fields = fieldMapArray)

        val result = mongoTemplate.save(survey, form.category!!.id.toString()) as SurveyDTO

        datasourceMockMvc.perform(get("/api/surveys/${result.id}?collectionId=${form.category!!.id}"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(result.id))
            .andExpect(jsonPath("$.formId").value(form.id.toString()))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun testUpdateDocumentByDocumentId() {
        val fieldMapArray = mutableListOf<Map<String, Any>>()
        fieldList.forEach {
            fieldMapArray.add(
                mapOf("key" to it.id.toString(), "value" to Math.random())
            )
        }

        val survey =
            SurveyDTO(companyId = company.id, formId = form.id, fields = fieldMapArray)

        val result = mongoTemplate.save(survey, form.category!!.id.toString())

        val updatedRow = survey.copy()

        val updatedFieldsMapArray = mutableListOf<Map<String, Any>>()
        fieldList.forEach { updatedFieldsMapArray.add(mapOf("key" to it.id.toString(), "value" to Math.random())) }
        updatedRow.fields = updatedFieldsMapArray

        datasourceMockMvc.perform(
            put("/api/surveys/${result.id}?collectionId=${form.category!!.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedRow))
        ).andExpect(status().isOk())

        val updatedResult = mongoTemplate.findById(
            result.id,
            SurveyDTO::class.java,
            form.category!!.id.toString()
        )
        assertThat(updatedResult!!.fields).isEqualTo(updatedRow.fields)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun testRemoveDocumentByDocumentId() {
        val fieldMapArray = mutableListOf<Map<String, Any>>()
        fieldList.forEach {
            fieldMapArray.add(
                mapOf("key" to it.id.toString(), "value" to Math.random())
            )
        }

        val survey =
            SurveyDTO(companyId = company.id, formId = form.id, fields = fieldMapArray)
        val result = mongoTemplate.save(survey, form.category!!.id.toString()) as SurveyDTO

        datasourceMockMvc.perform(delete("/api/surveys/${result.id}?collectionId=${form.category!!.id}"))
            .andExpect(status().isNoContent())

        val deletedResult = mongoTemplate.findById(
            result._id,
            Map::class.java,
            form.category!!.id.toString()
        )
        assertThat(deletedResult).isNull()
    }
}
