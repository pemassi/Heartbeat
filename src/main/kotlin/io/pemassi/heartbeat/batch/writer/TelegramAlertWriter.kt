package io.pemassi.heartbeat.batch.writer

import io.pemassi.kotlin.extensions.slf4j.getLogger
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

        val message: String

        if(item.result)
        {
            message = """
                Heartbeat Recovered Alert
                [$ruleName] Testing is now succeeding now :)
                
                Method      :   ${item.rule.test.method}
                Departure   :   ${item.departureHost}
                Destination :   ${item.destinationHost}
                
                ${item.alertMessage}
            """.trimIndent()
        }
        else
        {
            message = """
                !! HEARTBEAT ALERT !!
                [$ruleName] Testing is failing now. 
                
                Method      :   ${item.rule.test.method}
                Departure   :   ${item.departureHost}
                Destination :   ${item.destinationHost}
                
                ${item.alertMessage}
            """.trimIndent()
        }


        val result = client.send(botId, chatId, message).execute().isSuccessful

        logger.debug("[$ruleName] Telegram alert result - $result")
    }
}