package com.clangengineer.surveymodus.service.mapper

import com.clangengineer.surveymodus.domain.File
import com.clangengineer.surveymodus.service.dto.FileDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface FileMapper :
    EntityMapper<FileDTO, File> {

    override fun toDto(s: File): FileDTO
}
