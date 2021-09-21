package io.pemassi.heartbeat.models.rules.condition.details

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.condition.ConditionMethod

interface ConditionDetail {
    val method: ConditionMethod

    fun isMeetCondition(rule: HeartBeatRule): Boolean

    fun validation()
}