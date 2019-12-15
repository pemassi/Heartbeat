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
        val ip = testRule.ip ?: throw IllegalArgumentException("There is no ip address in the rule.")
        val timeout = testRule.timeout

        logger.debug("[$ruleName] Sending Ping Request to $ip")

        val pingResult = InetAddress.getByName(ip).isReachable(timeout)

        if (pingResult)
        {
            logger.debug("[$ruleName] Host is reachable")
        }
        else
        {
            logger.debug("[$ruleName] We can't reach to this host ($ip)")
        }

        return TestResult(pingResult, item)
    }
}