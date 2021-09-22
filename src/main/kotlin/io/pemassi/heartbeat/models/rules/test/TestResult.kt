package io.pemassi.heartbeat.models.rules.test

enum class TestResult
{
    SUCCESS,
    FAIL,
    ERROR,
}

fun Boolean.toTestResult(): TestResult
{
    return if(this)
        TestResult.SUCCESS
    else
        TestResult.FAIL
}