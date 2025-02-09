package com.clangengineer.surveymodus.service.dto

class StaffDTO(
    firstName: String? = null,
    lastName: String? = null,
    email: String? = null,
    activated: Boolean? = null,
    langKey: String? = null,
    var phone: String? = null,
    authorities: MutableSet<String> = mutableSetOf()
) : AdminUserDTO(
    login = null,
    id = null,
    firstName = firstName,
    lastName = lastName,
    email = email,
    imageUrl = null,
    activated = activated,
    langKey = langKey,
    createdBy = null,
    createdDate = null,
    lastModifiedBy = null,
    lastModifiedDate = null,
    authorities = authorities
)
