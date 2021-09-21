package io.pemassi.heartbeat.models.rules.alert.details

import io.pemassi.heartbeat.interfaces.TelegramAPI
import io.pemassi.heartbeat.models.rules.alert.AlertMethod
import io.pemassi.heartbeat.models.rules.test.TestResult
import io.pemassi.heartbeat.util.RestfulClient
import io.pemassi.kotlin.extensions.slf4j.getLogger
import kotlinx.serialization.Serializable

@Serializable
data class AlertTelegram(
    val botId: String,
    val chatId: Int,
): AlertDetail
{
    override val method: AlertMethod
        get() = AlertMethod.Telegram

    override fun validation() {

    }

    override fun reportConditionMet(testResult: TestResult) {
        report(testResult)
    }

    override fun reportRecovered(testResult: TestResult) {
        report(testResult)
    }

    private fun report(testResult: TestResult)
    {
        val client = RestfulClient.create(TelegramAPI::class)

        val result = client.send(botId, chatId, testResult.buildAlertTitleAndBody()).execute().isSuccessful

        logger.debug("[${testResult.rule.name}] Telegram alert result - $result")
    }

    companion object
    {
        private val logger by getLogger()
    }

}