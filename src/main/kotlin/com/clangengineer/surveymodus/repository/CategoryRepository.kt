package com.clangengineer.surveymodus.repository

import com.clangengineer.surveymodus.domain.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Suppress("unused")
@Repository
interface CategoryRepository : JpaRepository<Category, Long>, JpaSpecificationExecutor<Category>
