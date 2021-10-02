package io.pemassi.heartbeat.models.rules.test

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.test.details.TestDetail
import io.pemassi.heartbeat.service.AlertService
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
    fun reportConditionMet(alertService: AlertService)
    {
        rule.reportConditionMet(this, alertService)
    }

    fun reportRecovered(alertService: AlertService)
    {
        rule.reportRecovered(this, alertService)
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