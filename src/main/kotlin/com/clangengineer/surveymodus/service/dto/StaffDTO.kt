package com.clangengineer.surveymodus.service.dto

data class StaffDTO(
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var activated: Boolean? = null,
    var langKey: String? = null,
    var phone: String? = null,
    var authorities: MutableSet<String> = mutableSetOf()
)
