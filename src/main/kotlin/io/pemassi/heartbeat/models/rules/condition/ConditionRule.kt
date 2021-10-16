package io.pemassi.heartbeat.models.rules.condition

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.condition.details.ConditionDetail
import io.pemassi.heartbeat.models.rules.condition.details.ConditionFailMoreThan
import io.pemassi.heartbeat.models.rules.condition.details.ConditionPercentage
import kotlinx.serialization.Serializable
import org.springframework.context.ApplicationContext

@Serializable
data class ConditionRule
(
    val failMoreThan: ConditionFailMoreThan? = null,
    val percentage: ConditionPercentage? = null,
)
{
    val rules: List<ConditionDetail>
        get() = listOfNotNull(
            failMoreThan,
            percentage,
        )

    fun validation()
    {
        require(rules.isNotEmpty()) { "At least one condition rule needs."}
        require(rules.size == 1) { "Cannot use multiple conditions."}

        rules.forEach {
            it.validation()
        }
    }

    fun isMeetCondition(rule: HeartBeatRule, context: ApplicationContext): List<Boolean>
    {
        return rules.map { it.isMeetCondition(rule, context) }
    }
}