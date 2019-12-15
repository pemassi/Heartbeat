package io.pemassi.heartbeat.models.rules.test

import kotlinx.serialization.Serializable

@Serializable
data class TestRule(
    val method: TestMethod,
    val ip: String? = null,
    val timeout: Int = 5000,
    val port: String? = null,
    val url: String? = null
)
{

}