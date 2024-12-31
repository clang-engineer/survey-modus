package com.clangengineer.exformmaker.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.repository.FieldRepository
import com.clangengineer.surveymodus.repository.FormRepository
import com.clangengineer.surveymodus.web.rest.FieldResourceIT
import com.clangengineer.surveymodus.web.rest.FormResourceIT
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DatasourceControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var fieldRepository: FieldRepository

    @Autowired
    private lateinit var formRepository: FormRepository

    @Test
    @Transactional
    @Throws(Exception::class)
    fun testCreateFormRow() {
        val form = FormResourceIT.createEntity(em)
        em.persist(form)
        em.flush()

        for (i in 1..5) {
            val field = FieldResourceIT.createEntity(em)
            field.form = form
            em.persist(field)
            em.flush()
        }

        assertThat(formRepository.findAll().size).isEqualTo(1)
        assertThat(fieldRepository.findAll().size).isEqualTo(5)
    }
}
