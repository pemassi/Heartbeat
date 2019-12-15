package com.telcuon.appcard.restful.extension

val ByteArray.asHexLower get() = this.joinToString(separator = ""){ String.format("%02x",(it.toInt() and 0xFF))}
val ByteArray.asHexUpper get() = this.joinToString(separator = ""){ String.format("%02X",(it.toInt() and 0xFF))}
val String.hexAsByteArray get() = this.chunked(2).map { it.toUpperCase().toInt(16).toByte() }.toByteArray()