package io.pemassi.heartbeat.models.rules.test.details

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.test.TestMethod
import io.pemassi.heartbeat.models.rules.test.TestResult
import io.pemassi.kotlin.extensions.slf4j.getLogger
import kotlinx.serialization.Serializable
import java.net.InetSocketAddress
import java.net.Socket

@Serializable
data class TestTcp(
    val host: String,
    val port: Int,
    val timeout: Int = 5000
): TestDetail
{
    override val method: TestMethod
        get() = TestMethod.TCP

    override fun validation() {
        require(port in 0..65535)
    }

    override fun doTest(rule: HeartBeatRule): TestResult {
        val ruleName = rule.name

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
            testedRule = this,
            rule = rule
        )
    }

    companion object
    {
        private val logger by getLogger()
    }
}