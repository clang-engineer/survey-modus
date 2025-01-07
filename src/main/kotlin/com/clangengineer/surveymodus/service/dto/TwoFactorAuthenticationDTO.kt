package com.clangengineer.surveymodus.service.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class TwoFactorAuthenticationDTO(
    @get: NotNull
    @get: Pattern(regexp = "^01[0-9]-\\d{3,4}-\\d{4}\$", message = "전화번호는 01x-XXX(XX)-XXXX 형식이어야 합니다.")
    val phoneNumber: String = "010-1234-5678"
) {
    fun toKey(): String {
        return "staff:$phoneNumber"
    }
}
