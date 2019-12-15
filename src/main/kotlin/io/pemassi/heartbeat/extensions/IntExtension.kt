package com.telcuon.appcard.restful.extension

val Int.asByteArray get() =
    byteArrayOf(
            (this shr 24).toByte(),
            (this shr 16).toByte(),
            (this shr 8).toByte(),
            this.toByte())

@ExperimentalUnsignedTypes
val ByteArray.asInt get() =
        ((this[0].toUInt() and 0xFFu) shl 24) or
        ((this[1].toUInt() and 0xFFu) shl 16) or
        ((this[2].toUInt() and 0xFFu) shl 8) or
        (this[3].toUInt() and 0xFFu)