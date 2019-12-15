package com.telcuon.appcard.restful.extension

import java.security.MessageDigest

val String.asSHA256 get() = SHA256(this)

operator fun String.get(range: IntRange) : String
{
    return this.substring(range)
}

fun SHA256(string: String) : String
{
    val bytes = string.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)

    return digest.asHexUpper
}