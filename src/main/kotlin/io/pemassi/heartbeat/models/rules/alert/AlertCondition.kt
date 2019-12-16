package io.pemassi.heartbeat.models.rules.alert

import kotlinx.serialization.Serializable

@Serializable
data class AlertCondition
(
    val method: AlertConditionMethod,
    val times: Int? = null
)