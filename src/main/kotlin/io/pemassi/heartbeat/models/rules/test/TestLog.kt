package io.pemassi.heartbeat.models.rules.test

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.test.details.TestDetail
import org.springframework.context.ApplicationContext
import java.net.InetAddress

data class TestLog
(
    val rule: HeartBeatRule,
    val testDetail: TestDetail,
    var result: TestResult,
    val departureHostAddress: String = InetAddress.getLocalHost().hostAddress,
    val departureHostName: String = InetAddress.getLocalHost().hostName,
    val departurePort: Int? = null,
    val destinationHost: String? = null,
    val destinationPort: Int? = null,
    val errorMessage: String? = null,
    val additionalParamMap: HashMap<String, String> = HashMap()
)
{
    fun reportConditionMet(context: ApplicationContext)
    {
        rule.reportConditionMet(this, context)
    }

    fun reportRecovered(context: ApplicationContext)
    {
        rule.reportRecovered(this, context)
    }

    fun buildAlertTitle(): String
    {
        return if(result == TestResult.SUCCESS)
        {
            """
                Heartbeat Recovered Alert
            """.trimIndent()
        }
        else
        {
            """
                !! HEARTBEAT ALERT !!
            """.trimIndent()
        }
    }

    fun buildAlertBody(): String
    {
        return if(result == TestResult.SUCCESS)
        {
            """
                [${rule.name}] Testing is now succeeding now :)
                    
                Method      :   ${testDetail.method}
                Departure   :   $departureHostAddress
                Destination :   $destinationHost
                
            """.trimIndent()
        }
        else
        {
            """
                [${rule.name}] Testing is failing now. 
                
                Method      :   ${testDetail.method}
                Departure   :   ${departureHostAddress}
                Destination :   ${destinationHost}
                
            """.trimIndent()
        }
    }

    fun buildAlertTitleAndBody(): String
    {
        return """
            ${buildAlertTitle()}
            
            ${buildAlertBody()}
        """.trimIndent()
    }
}