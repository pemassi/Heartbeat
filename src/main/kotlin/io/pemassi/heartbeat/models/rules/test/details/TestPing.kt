package io.pemassi.heartbeat.models.rules.test.details

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.const.HeartbeatConst
import io.pemassi.heartbeat.models.rules.test.TestLog
import io.pemassi.heartbeat.models.rules.test.TestMethod
import io.pemassi.heartbeat.models.rules.test.toTestResult
import io.pemassi.kotlin.extensions.slf4j.getLogger
import kotlinx.serialization.Serializable
import java.net.InetAddress

@Serializable
data class TestPing(
    val host: String,
    val timeout: Int = 5000,
): TestDetail()
{
    override val method: TestMethod
        get() = TestMethod.Ping

    override fun validation() {
        //Nothing
    }

    override fun doTest(rule: HeartBeatRule): TestLog {
        val ruleName = rule.name
        val additionalParamMap = HashMap<String, String>()

        logger.debug("[$ruleName] Sending Ping Request to $host")

        val pingResult = InetAddress.getByName(host).isReachable(timeout)

        if (pingResult)
        {
            logger.debug("[$ruleName] Host is reachable")
        }
        else
        {
            logger.debug("[$ruleName] We can't reach to this host ($host)")
            additionalParamMap[HeartbeatConst.Param.FAIL_MESSAGE] = " Cannot reach to host. (Timeout in $timeout ms)"
        }

        return TestLog(
            result = pingResult.toTestResult(),
            destinationHost = host,
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