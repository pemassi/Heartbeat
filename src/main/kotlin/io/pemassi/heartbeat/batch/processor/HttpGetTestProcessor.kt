package io.pemassi.heartbeat.batch.processor

import io.pemassi.heartbeat.models.TestResult
import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.kotlin.extensions.slf4j.getLogger
import org.springframework.batch.item.ItemProcessor


class HttpGetTestProcessor: ItemProcessor<HeartBeatRule, TestResult>
{
    private val logger by getLogger()

    override fun process(item: HeartBeatRule): TestResult
    {
        val ruleName = item.name
        val testRule = item.test

        val url = testRule.url ?: throw IllegalArgumentException("There is no URL in the rule.")
        val timeout = testRule.timeout

        logger.debug("[$ruleName] HTTP Get test to $url")

        var testingResult: Boolean
        var alertMessage: String? = null

        try
        {
            logger.debug("[$ruleName] Success to connect to $url")

            TODO("Need to add restful testing here.")

            testingResult = true
        }
        catch (e: Exception)
        {
            logger.error("[$ruleName] Fail to connect to $url", e)

            testingResult = false
            alertMessage = """
                Cannot connect socket.
                
                ${e.localizedMessage}
            """.trimIndent()
        }

        return TestResult(
                result = testingResult,
                destinationHost = "$url",
                alertMessage = alertMessage,
                rule = item
        )
    }
}