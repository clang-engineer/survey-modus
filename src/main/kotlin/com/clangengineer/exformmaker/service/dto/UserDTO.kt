package com.clangengineer.exformmaker.service.dto

import com.clangengineer.exformmaker.domain.User
import java.io.Serializable

/**
 * A DTO representing a user, with only the public attributes.
 */
open class UserDTO(
    var id: Long? = null,
    var login: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
) : Serializable {

    constructor(user: User) : this(user.id, user.login)

    override fun toString() = "UserDTO{" +
        "login='" + login + '\'' +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
