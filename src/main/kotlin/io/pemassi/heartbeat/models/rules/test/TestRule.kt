package io.pemassi.heartbeat.models.rules.test

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.test.details.TestDetail
import io.pemassi.heartbeat.models.rules.test.details.TestPing
import io.pemassi.heartbeat.models.rules.test.details.TestTcp
import io.pemassi.kotlin.extensions.slf4j.getLogger
import kotlinx.serialization.Serializable

@Serializable
data class TestRule(
    val ping: TestPing? = null,
    val tcp: TestTcp? = null,
)
{
    val rules: List<TestDetail>
        get() = listOfNotNull(ping, tcp)

    fun validation()
    {
        require(rules.isNotEmpty()) { "At least one test rule needs."}

        rules.forEach {
            it.validation()
        }
    }

    fun performTest(rule: HeartBeatRule): List<TestResult>
    {
        return this.rules.map {
            logger.debug("[${rule.name}] Try to test with methods [${it.method}]")

            it.doTest(rule)
        }
    }

    companion object
    {
        private val logger by getLogger()
    }
}