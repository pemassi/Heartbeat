package io.pemassi.heartbeat.batch.processor

import io.pemassi.kotlin.extensions.slf4j.getLogger
import io.pemassi.heartbeat.models.TestResult
import io.pemassi.heartbeat.models.rules.HeartBeatRule
import org.springframework.batch.item.ItemProcessor
import java.net.InetSocketAddress
import java.net.Socket


class SocketTestProcessor: ItemProcessor<HeartBeatRule, TestResult>
{
    private val logger by getLogger()

    override fun process(item: HeartBeatRule): TestResult
    {
        val ruleName = item.name
        val testRule = item.test

        val host = testRule.host ?: throw IllegalArgumentException("There is no host in the rule.")
        val port = testRule.port ?: throw IllegalArgumentException("There is no port number in the rule.")
        val timeout = testRule.timeout

        logger.debug("[$ruleName] Socket connection test to $host:$port")

        var testingResult: Boolean
        var alertMessage: String? = null

        try
        {
            Socket().use {
                val endPoint = InetSocketAddress(host, port)
                it.connect(endPoint, timeout)
            }

            logger.debug("[$ruleName] Success to connect to $host:$port")

            testingResult = true
        }
        catch (e: Exception)
        {
            logger.error("[$ruleName] Fail to connect to $host:$port.", e)

            testingResult = false
            alertMessage = """
                Cannot connect socket.
                
                ${e.localizedMessage}
            """.trimIndent()
        }

        return TestResult(
                result = testingResult,
                destinationHost = "$host:$port",
                alertMessage = alertMessage,
                rule = item
        )
    }
}