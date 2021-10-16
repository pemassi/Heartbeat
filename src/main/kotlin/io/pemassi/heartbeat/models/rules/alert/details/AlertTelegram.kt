package io.pemassi.heartbeat.models.rules.alert.details

import io.pemassi.heartbeat.api.TelegramApi
import io.pemassi.heartbeat.api.dto.telegram.SendMessageDto
import io.pemassi.heartbeat.models.rules.alert.AlertMethod
import io.pemassi.heartbeat.models.rules.test.TestLog
import io.pemassi.kotlin.extensions.slf4j.getLogger
import io.pemassi.kotlin.extensions.spring.getBean
import kotlinx.serialization.Serializable
import org.springframework.context.ApplicationContext

@Serializable
data class AlertTelegram(
    val botId: String,
    val chatId: Int,
): AlertDetail()
{
    override val method: AlertMethod
        get() = AlertMethod.Telegram

    override fun validation() {

    }

    override fun reportConditionMet(testLog: TestLog, context: ApplicationContext) {
        report(testLog, context)
    }

    override fun reportRecovered(testLog: TestLog, context: ApplicationContext) {
        report(testLog, context)
    }

    private fun report(testLog: TestLog, context: ApplicationContext)
    {
        val telegramApi = context.getBean(TelegramApi::class)
        val result = telegramApi.send(
            botId = botId,
            dto = SendMessageDto(
                chatId = chatId,
                text =  testLog.buildAlertTitleAndBody()
            )
        )

        logger.debug("[${testLog.rule.name}] Telegram alert result - $result")
    }

    companion object
    {
        private val logger by getLogger()
    }

}