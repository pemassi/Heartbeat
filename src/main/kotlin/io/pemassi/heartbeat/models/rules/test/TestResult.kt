package io.pemassi.heartbeat.models.rules.test

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.test.details.TestDetail
import java.net.InetAddress

data class TestResult
(
    val result: Boolean,
    val departureHost: String = InetAddress.getLocalHost().let { "${it.hostAddress} (${it.hostName})" },
    val destinationHost: String,
    val alertMessage: String?,
    val testedRule: TestDetail,
    val rule: HeartBeatRule
)
{
    fun reportConditionMet()
    {
        rule.reportConditionMet(this)
    }

    fun reportRecovered()
    {
        rule.reportRecovered(this)
    }

    fun buildAlertTitle(): String
    {
        return if(result)
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
        return if(result)
        {
            """
                [${rule.name}] Testing is now succeeding now :)
                    
                Method      :   ${testedRule.method}
                Departure   :   $departureHost
                Destination :   $destinationHost
                
                $alertMessage
            """.trimIndent()
        }
        else
        {
            """
                [${rule.name}] Testing is failing now. 
                
                Method      :   ${testedRule.method}
                Departure   :   ${departureHost}
                Destination :   ${destinationHost}
                
                ${alertMessage}
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