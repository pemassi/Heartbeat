package io.pemassi.heartbeat.models.rules.condition

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.condition.details.ConditionDetail
import io.pemassi.heartbeat.models.rules.condition.details.ConditionFailMoreThan
import kotlinx.serialization.Serializable

@Serializable
data class ConditionRule
(
    val failMoreThan: ConditionFailMoreThan? = null,
)
{
    val rules: List<ConditionDetail>
        get() = listOfNotNull(failMoreThan)

    fun validation()
    {
        require(rules.isNotEmpty()) { "At least one condition rule needs."}
        require(rules.size == 1) { "Cannot use multiple conditions."}

        rules.forEach {
            it.validation()
        }
    }

    fun isMeetCondition(rule: HeartBeatRule): List<Boolean>
    {
        return rules.map { it.isMeetCondition(rule) }
    }
}