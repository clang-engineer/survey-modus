package com.clangengineer.surveymodus.service

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

fun getSHA512(input: String): String {
    val digest = MessageDigest.getInstance("SHA-512")
    val hash = digest.digest(input.toByteArray(StandardCharsets.UTF_8))

    return hash.joinToString("") { "%02x".format(it) }
}
