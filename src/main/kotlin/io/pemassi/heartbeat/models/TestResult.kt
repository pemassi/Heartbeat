package io.pemassi.heartbeat.models

import io.pemassi.heartbeat.models.rules.HeartBeatRule

data class TestResult
(
    val result: Boolean,
    val rule: HeartBeatRule
)