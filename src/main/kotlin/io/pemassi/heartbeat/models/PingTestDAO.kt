package io.pemassi.heartbeat.models

data class PingTestDAO
(
    val ip: String,
    val timeout: Int = 5000
)