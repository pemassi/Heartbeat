package io.pemassi.heartbeat.models.rules.alert

import kotlinx.serialization.Serializable

@Serializable
data class AlertRule(
    val method: AlertMethod,
    val botId: String? = null,
    val chatId: Int? = null,
    val telegramMessage: String? = null
)