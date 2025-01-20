package com.clangengineer.surveymodus.service.dto

import org.bson.types.ObjectId

data class MessageDTO(
    val _id: ObjectId? = null,
    val message: String,
    val companyId: Long,
) {
    val id: String
        get() = _id?.toHexString() ?: ""
}
