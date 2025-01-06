package com.clangengineer.surveymodus.service.dto

import javax.validation.constraints.NotBlank

data class TwoFactorAuthenticationDTO(
    @field:NotBlank
    val phoneNumber: String = "010-1234-5678"
) {
    fun toKey(): String {
        return "staff:$phoneNumber"
    }
}
