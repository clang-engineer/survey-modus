package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.config.DOCUMENT_FORM_ID
import com.clangengineer.surveymodus.domain.Field
import com.clangengineer.surveymodus.domain.embeddable.FieldAttribute
import com.clangengineer.surveymodus.domain.enumeration.type
import com.clangengineer.surveymodus.service.dto.DocumentDTO
import com.clangengineer.surveymodus.service.dto.FieldDTO
import com.clangengineer.surveymodus.service.dto.FormDTO
import com.clangengineer.surveymodus.service.mapper.FieldMapper
import com.clangengineer.surveymodus.service.mapper.FormMapper
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
@TestPropertySource(properties = ["spring.mongodb.embedded.version=4.0.3"])
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
    fun testCreateFormRow() {
        val row = mutableMapOf<String, Any>()
        fieldList.forEach { row[it.id.toString()] = Math.random() }

        val document = DocumentDTO(form, row)

        datasourceMockMvc.perform(post("/api/documents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(document)))
            .andExpect(status().isCreated())

        val result = mongoTemplate.findAll(Map::class.java, form.category!!.id.toString())
        assertThat(result).hasSize(1)
        assertThat(result[0][DOCUMENT_FORM_ID]).isEqualTo(form.id.toString())
        for (field in fieldList) {
            assertThat(result[0][field.id.toString()]).isEqualTo(row[field.id.toString()])
        }
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun testFindAllFormRows() {
        for (i in 1..5) {
            val row = mutableMapOf<String, Any>()
            fieldList.forEach { row[it.id.toString()] = Math.random() }
            mongoTemplate.save(row, form.category!!.id.toString())
        }

        datasourceMockMvc.perform(
            get("/api/documents/${form.category!!.id}")
        ).andExpect(status().isOk())
    }
}
