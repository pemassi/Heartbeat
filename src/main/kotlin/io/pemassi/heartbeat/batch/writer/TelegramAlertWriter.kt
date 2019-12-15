package io.pemassi.heartbeat.batch.writer

import com.telcuon.appcard.restful.extension.getLogger
import io.pemassi.heartbeat.interfaces.TelegramAPI
import io.pemassi.heartbeat.models.TestResult
import io.pemassi.heartbeat.util.RestfulClient

class TelegramAlertWriter: OneItemWriter<TestResult>
{
    private val logger by getLogger()

    override fun write(item: TestResult)
    {
        val client = RestfulClient.create(TelegramAPI::class)

        val alertRule = item.rule.alert
        val ruleName = item.rule.name
        val botId = alertRule.botId ?: throw IllegalArgumentException("There is no bot id in the rule.")
        val chatId = alertRule.chatId ?: throw IllegalArgumentException("There is no chat id in the rule.")

        val message = """
            [$ruleName] ALERT

            Fail to test with method : [${item.rule.test.method}]
        """.trimIndent()

        val result = client.send(botId, chatId, message).execute().isSuccessful

        logger.debug("[$ruleName] Telegram alert result - $result")
    }
}