package io.pemassi.heartbeat.models.rules.condition.details

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.condition.ConditionMethod
import io.pemassi.heartbeat.service.ConditionService
import kotlinx.serialization.Serializable

@Serializable
sealed class ConditionDetail {
    abstract val method: ConditionMethod

    abstract fun isMeetCondition(rule: HeartBeatRule, conditionService: ConditionService): Boolean

    abstract fun validation()
}