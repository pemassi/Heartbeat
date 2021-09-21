package io.pemassi.heartbeat.models.rules.condition.details

import io.pemassi.heartbeat.global.TestTracking
import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.condition.ConditionMethod
import kotlinx.serialization.Serializable

@Serializable
data class ConditionFailMoreThan(
    val times: Int
): ConditionDetail {

    override val method: ConditionMethod
        get() = ConditionMethod.FailMoreThan

    override fun validation() {

    }

    override fun isMeetCondition(rule: HeartBeatRule): Boolean {
        return TestTracking.getFailCount(rule.name) > times
    }
}