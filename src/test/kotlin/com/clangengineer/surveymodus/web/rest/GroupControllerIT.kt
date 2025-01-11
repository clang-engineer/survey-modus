package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.repository.GroupRepository
import com.clangengineer.surveymodus.service.mapper.GroupMapper
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
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
class GroupControllerIT {
    @Autowired
    private lateinit var groupRepository: GroupRepository

    @Autowired
    private lateinit var groupMapper: GroupMapper

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createAndUpdateAllGroupsTest() {
        val group1 = GroupResourceIT.createEntity(em)
        em.persist(group1)
        em.flush()

        val group2 = GroupResourceIT.createEntity(em)

        val groupsDTO = listOf(group1, group2).map { groupMapper.toDto(it) }

        val databaseSizeBeforeBulkInsert = groupRepository.findAll().size

        mockMvc.perform(
            put("/api/groups/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(groupsDTO))
        ).andExpect(status().isOk)

        val groupList = groupRepository.findAll()
        assertThat(groupList).hasSize(databaseSizeBeforeBulkInsert + 1)

        val updated = groupRepository.findById(group1.id!!).get()
        assertThat(updated).isEqualTo(group1)
    }
}
