package io.pemassi.heartbeat.util

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.KSerializer
import java.io.File


object YamlParser
{
    fun <T> parse(file: File, clazz: KSerializer<T>): T
    {
        val stream = file.inputStream()

        return Yaml.default.decodeFromStream(clazz, stream).also {
            stream.close()
        }
    }
}