package io.pemassi.heartbeat.models.rules.test.details

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.const.HeartbeatConst
import io.pemassi.heartbeat.models.rules.test.TestLog
import io.pemassi.heartbeat.models.rules.test.TestMethod
import io.pemassi.heartbeat.models.rules.test.toTestResult
import io.pemassi.kotlin.extensions.slf4j.getLogger
import kotlinx.serialization.Serializable
import org.hibernate.validator.constraints.Range
import org.springframework.context.ApplicationContext
import java.net.InetSocketAddress
import java.net.Socket

@Serializable
data class TestTcp(
    val host: String,
    @Range(min = 1, max = 65535)
    val port: Int,
    val timeout: Int = 5000
): TestDetail()
{
    override val method: TestMethod
        get() = TestMethod.TCP

    override fun validation() {

    }

    override fun performTest(rule: HeartBeatRule, context: ApplicationContext): TestLog {
        val ruleName = rule.name
        val additionalParamMap = HashMap<String, String>()

        logger.debug("[$ruleName] Socket connection test to $host:$port")

        var testingResult: Boolean

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
            additionalParamMap[HeartbeatConst.Param.FAIL_MESSAGE] = e.localizedMessage
        }

        return TestLog(
            result = testingResult.toTestResult(),
            destinationHost = host,
            destinationPort = port,
            testDetail = this,
            rule = rule,
            additionalParamMap = additionalParamMap,
        )
    }

    companion object
    {
        private val logger by getLogger()
    }
}