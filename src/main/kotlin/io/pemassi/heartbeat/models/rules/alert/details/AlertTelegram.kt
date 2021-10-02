package io.pemassi.heartbeat.models.rules.alert.details

import io.pemassi.heartbeat.interfaces.TelegramAPI
import io.pemassi.heartbeat.models.rules.alert.AlertMethod
import io.pemassi.heartbeat.models.rules.test.TestLog
import io.pemassi.heartbeat.service.AlertService
import io.pemassi.heartbeat.util.RestfulClient
import io.pemassi.kotlin.extensions.slf4j.getLogger
import kotlinx.serialization.Serializable

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

    override fun reportConditionMet(testLog: TestLog, alertService: AlertService) {
        report(testLog, alertService)
    }

    override fun reportRecovered(testLog: TestLog, alertService: AlertService) {
        report(testLog, alertService)
    }

    private fun report(testLog: TestLog, alertService: AlertService)
    {
        val client = RestfulClient.create(TelegramAPI::class)

        val result = client.send(botId, chatId, testLog.buildAlertTitleAndBody()).execute().isSuccessful

        logger.debug("[${testLog.rule.name}] Telegram alert result - $result")
    }

    companion object
    {
        private val logger by getLogger()
    }

}