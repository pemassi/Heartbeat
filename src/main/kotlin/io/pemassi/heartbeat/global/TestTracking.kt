package io.pemassi.heartbeat.global

import io.pemassi.kotlin.extensions.slf4j.getLogger

object TestTracking
{
    private val logger by getLogger()

    private val failingCountHashMap = HashMap<String, Int>()
    private val alertedHashMap = HashSet<String>()

    fun upFailCount(ruleName: String, increment: Int = 1)
    {
        val count = (failingCountHashMap[ruleName] ?: 0) + increment
        failingCountHashMap[ruleName] = count
    }

    fun getFailCount(ruleName: String): Int
    {
        return failingCountHashMap[ruleName] ?: 0
    }

    fun isAlerted(ruleName: String): Boolean
    {
        return alertedHashMap.contains(ruleName)
    }

    fun alerted(ruleName: String)
    {
        alertedHashMap.add(ruleName)
    }

    fun recovered(ruleName: String)
    {
        alertedHashMap.remove(ruleName)
    }
}