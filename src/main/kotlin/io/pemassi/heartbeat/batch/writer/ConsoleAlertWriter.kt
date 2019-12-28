package io.pemassi.heartbeat.batch.writer

import com.telcuon.appcard.restful.extension.getLogger
import io.pemassi.heartbeat.models.TestResult

class ConsoleAlertWriter: OneItemWriter<TestResult>
{
    private val logger by getLogger()

    override fun write(item: TestResult)
    {
        val ruleName = item.rule.name

        val message: String

        if(item.result)
        {
            message = """
                Heartbeat Recovered Alert
                [$ruleName] Testing is now succeeding now :)
                
                Method      :   ${item.rule.test.method}
                Departure   :   ${item.departureHost}
                Destination :   ${item.destinationHost}
                
                ${item.alertMessage}
            """.trimIndent()
        }
        else
        {
            message = """
                !! HEARTBEAT ALERT !!
                [$ruleName] Testing is failing now. 
                
                Method      :   ${item.rule.test.method}
                Departure   :   ${item.departureHost}
                Destination :   ${item.destinationHost}
                
                ${item.alertMessage}
            """.trimIndent()
        }

        logger.info("[$ruleName] Console Alerting\n$message")
    }
}