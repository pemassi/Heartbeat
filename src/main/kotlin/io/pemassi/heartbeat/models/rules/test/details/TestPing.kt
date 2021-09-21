package io.pemassi.heartbeat.models.rules.test.details

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.test.TestMethod
import io.pemassi.heartbeat.models.rules.test.TestResult
import io.pemassi.kotlin.extensions.slf4j.getLogger
import kotlinx.serialization.Serializable
import java.net.InetAddress

@Serializable
data class TestPing(
    val host: String,
    val timeout: Int = 5000,
): TestDetail
{
    override val method: TestMethod
        get() = TestMethod.Ping

    override fun validation() {
        TODO("Not yet implemented")
    }

    override fun doTest(rule: HeartBeatRule): TestResult {
        val ruleName = rule.name

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
            testedRule = this,
            rule = rule
        )
    }

    companion object
    {
        private val logger by getLogger()
    }
}