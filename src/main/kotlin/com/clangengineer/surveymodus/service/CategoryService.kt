package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.repository.CategoryRepository
import com.clangengineer.surveymodus.service.dto.CategoryDTO
import com.clangengineer.surveymodus.service.mapper.CategoryMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class CategoryService(private val categoryRepository: CategoryRepository, private val categoryMapper: CategoryMapper) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun save(categoryDTO: CategoryDTO): CategoryDTO {
        log.debug("Request to save Category : $categoryDTO")

        var category = categoryMapper.toEntity(categoryDTO)
        category = categoryRepository.save(category)
        return categoryMapper.toDto(category)
    }

    fun update(categoryDTO: CategoryDTO): CategoryDTO {
        log.debug("Request to update Category : {}", categoryDTO)

        var category = categoryMapper.toEntity(categoryDTO)
        category = categoryRepository.save(category)
        return categoryMapper.toDto(category)
    }

    fun partialUpdate(categoryDTO: CategoryDTO): Optional<CategoryDTO> {
        log.debug("Request to partially update Category : {}", categoryDTO)

        return categoryRepository.findById(categoryDTO.id)
            .map {
                categoryMapper.partialUpdate(it, categoryDTO)
                it
            }
            .map { categoryRepository.save(it) }
            .map { categoryMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<CategoryDTO> {
        log.debug("Request to get all Categorys")

        return categoryRepository.findAll(pageable).map(categoryMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<CategoryDTO> {
        log.debug("Request to get Category : $id")

        return categoryRepository.findById(id).map(categoryMapper::toDto)
    }

    fun delete(id: Long) {
        log.debug("Request to delete Category : $id")

        categoryRepository.deleteById(id)
    }
}
