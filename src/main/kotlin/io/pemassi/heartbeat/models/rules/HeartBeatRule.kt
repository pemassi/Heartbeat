package io.pemassi.heartbeat.models.rules

import io.pemassi.heartbeat.models.rules.alert.AlertRule
import io.pemassi.heartbeat.models.rules.test.TestRule
import kotlinx.serialization.Serializable

@Serializable
data class HeartBeatRule
(
        val name: String,
        val description: String = "",
        val test: TestRule,
        val alert: AlertRule
)