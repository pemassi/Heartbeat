package io.pemassi.heartbeat.batch.processor

import com.telcuon.appcard.restful.extension.getLogger
import io.pemassi.heartbeat.models.TestResult
import io.pemassi.heartbeat.models.rules.HeartBeatRule
import org.springframework.batch.item.ItemProcessor
import java.net.InetAddress

class PingTestProcessor: ItemProcessor<HeartBeatRule, TestResult>
{
    private val logger by getLogger()

    override fun process(item: HeartBeatRule): TestResult
    {
        val ruleName = item.name
        val testRule = item.test
        val host = testRule.host ?: throw IllegalArgumentException("There is no host in the rule.")
        val timeout = testRule.timeout

        logger.debug("[$ruleName] Sending Ping Request to $host")

        val pingResult = InetAddress.getByName(host).isReachable(timeout)
        var alertMessage: String? = null

        if (pingResult)
        {
            logger.debug("[$ruleName] Host is reachable")
        }
        else
        {
            logger.debug("[$ruleName] We can't reach to this host ($host)")
            alertMessage = """
                Cannot reach to host. (Timeout in $timeout ms)
            """.trimIndent()
        }

        return TestResult(
                result = pingResult,
                destinationHost = host,
                alertMessage = alertMessage,
                rule = item
        )
    }
}