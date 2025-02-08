package com.clangengineer.surveymodus.service.dto

import org.bson.types.ObjectId
import java.time.Instant

data class MessageDTO(
    val _id: ObjectId? = null,
    val companyId: Long? = null,
    val content: String? = null,
    val createdBy: String? = null,
    val createdDate: Instant? = null,
) {
    val id: String
        get() = _id?.toHexString() ?: ""
}
