package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.config.DOCUMENT_COMPANY_ID
import com.clangengineer.surveymodus.config.DOCUMENT_FORM_ID
import com.clangengineer.surveymodus.config.DOCUMENT_ID
import com.clangengineer.surveymodus.domain.Field
import com.clangengineer.surveymodus.domain.embeddable.FieldAttribute
import com.clangengineer.surveymodus.domain.enumeration.type
import com.clangengineer.surveymodus.service.dto.FieldDTO
import com.clangengineer.surveymodus.service.dto.FormDTO
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
class DocumentControllerIT {

    @Autowired
    private lateinit var datasourceMockMvc: MockMvc

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var formMapper: FormMapper

    @Autowired
    private lateinit var fieldMapper: FieldMapper

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

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
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun testCreateDocumentInCollection() {
        val document = mutableMapOf<String, Any>()
        fieldList.forEach { document[it.id.toString()] = Math.random() }
        document[DOCUMENT_FORM_ID] = form.id.toString()

        datasourceMockMvc.perform(
            post("/api/collections/${form.category!!.id}/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(document))
        )
            .andExpect(status().isCreated())

        val result = mongoTemplate.findAll(Map::class.java, form.category!!.id.toString())
        assertThat(result).hasSize(1)
        assertThat(result[0][DOCUMENT_FORM_ID]).isEqualTo(form.id.toString())
        for (field in fieldList) {
            assertThat(result[0][field.id.toString()]).isEqualTo(document[field.id.toString()])
        }
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun testFindAllDocumentsInCollectionsByFormId() {
        val companyId = 9999999999
        for (i in 1..5) {
            val row = mutableMapOf<String, Any>()
            fieldList.forEach { row[it.id.toString()] = Math.random() }
            row[DOCUMENT_FORM_ID] = form.id!!
            row[DOCUMENT_COMPANY_ID] = companyId
            mongoTemplate.save(row, form.category!!.id.toString())
        }

        val API_URI = "/api/collections/${form.category!!.id}/documents?companyId=$companyId&formId=${form.id}"
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
        val row = mutableMapOf<String, Any>()
        fieldList.forEach { row[it.id.toString()] = Math.random() }
        row[DOCUMENT_FORM_ID] = form.id.toString()
        val result = mongoTemplate.save(row, form.category!!.id.toString())

        datasourceMockMvc.perform(get("/api/collections/${form.category!!.id}/documents/${result[DOCUMENT_ID]}"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$._id").value(result[DOCUMENT_ID].toString()))
            .andExpect(jsonPath("$.formId").value(form.id.toString()))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun testUpdateDocumentByDocumentId() {
        val row = mutableMapOf<String, Any>()
        fieldList.forEach { row[it.id.toString()] = Math.random() }
        row[DOCUMENT_FORM_ID] = form.id.toString()
        val result = mongoTemplate.save(row, form.category!!.id.toString())

        val updatedRow = result.toMutableMap()
        fieldList.forEach { updatedRow[it.id.toString()] = Math.random() }

        datasourceMockMvc.perform(
            put("/api/collections/${form.category!!.id}/documents/${result[DOCUMENT_ID]}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedRow))
        ).andExpect(status().isOk())

        val updatedResult = mongoTemplate.findById(result[DOCUMENT_ID], Map::class.java, form.category!!.id.toString())
        for (field in fieldList) {
            assertThat(updatedResult!![field.id.toString()]).isEqualTo(updatedRow[field.id.toString()])
        }
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun testRemoveDocumentByDocumentId() {
        val row = mutableMapOf<String, Any>()
        fieldList.forEach { row[it.id.toString()] = Math.random() }
        row[DOCUMENT_FORM_ID] = form.id.toString()
        val result = mongoTemplate.save(row, form.category!!.id.toString())

        datasourceMockMvc.perform(delete("/api/collections/${form.category!!.id}/documents/${result[DOCUMENT_ID]}"))
            .andExpect(status().isNoContent())

        val deletedResult = mongoTemplate.findById(result[DOCUMENT_ID], Map::class.java, form.category!!.id.toString())
        assertThat(deletedResult).isNull()
    }
}
