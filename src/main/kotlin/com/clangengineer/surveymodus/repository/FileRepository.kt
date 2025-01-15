package com.clangengineer.surveymodus.repository

import com.clangengineer.surveymodus.domain.File
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface FileRepository : JpaRepository<File, Long>, JpaSpecificationExecutor<File>
