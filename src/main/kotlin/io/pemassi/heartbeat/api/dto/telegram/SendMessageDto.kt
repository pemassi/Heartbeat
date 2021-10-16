package io.pemassi.heartbeat.api.dto.telegram

data class SendMessageDto(
    val chatId: Int,
    val text: String,
    val parseMode: String? = null,
    val disableWebPagePreview: Boolean? = null,
    val disableNotification: Boolean? = null,
    val replyToMessageId: Int? = null,
    val replyMarkup: Any? = null
)
