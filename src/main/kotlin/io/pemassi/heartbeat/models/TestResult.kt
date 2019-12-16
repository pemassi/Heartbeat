package io.pemassi.heartbeat.models

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import java.net.InetAddress

data class TestResult
(
        val result: Boolean,
        val departureHost: String = InetAddress.getLocalHost().let { "${it.hostAddress} (${it.hostName})" },
        val destinationHost: String,
        val alertMessage: String?,
        val rule: HeartBeatRule
)