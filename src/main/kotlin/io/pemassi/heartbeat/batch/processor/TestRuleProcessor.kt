package io.pemassi.heartbeat.batch.processor

import com.telcuon.appcard.restful.extension.getLogger
import io.pemassi.heartbeat.models.TestResult
import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.test.TestMethod
import org.springframework.batch.item.ItemProcessor

class TestRuleProcessor: ItemProcessor<HeartBeatRule, TestResult>
{
    private val logger by getLogger()

    override fun process(item: HeartBeatRule): TestResult
    {
        val ruleName = item.name
        val method = item.test.method

        logger.debug("[$ruleName] Try to test with method [$method]")

        return when(method)
        {
            TestMethod.Ping -> PingTestProcessor().process(item)
            TestMethod.Socket -> SocketTestProcessor().process(item)

            else -> throw Exception("Unsupported test method.")
        }
    }
}