package io.pemassi.heartbeat.models.rules.condition.details

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.condition.ConditionMethod
import kotlinx.serialization.Serializable
import org.springframework.context.ApplicationContext

@Serializable
sealed class ConditionDetail {
    abstract val method: ConditionMethod

    abstract fun isMeetCondition(rule: HeartBeatRule, context: ApplicationContext): Boolean

    abstract fun validation()
}