package io.pemassi.heartbeat.batch.writer

import com.telcuon.appcard.restful.extension.getLogger
import io.pemassi.heartbeat.models.TestResult
import io.pemassi.heartbeat.models.rules.alert.AlertMethod
import org.springframework.batch.item.ItemWriter

class AlertRuleWriter: ItemWriter<TestResult>
{
    private val logger by getLogger()

    override fun write(items: MutableList<out TestResult>)
    {
        for (item in items)
        {
            val ruleName = item.rule.name

            if(item.result)
            {
                logger.debug("[$ruleName] Pass")
            }
            else
            {
                logger.error("[$ruleName] Fail, try to alert.")

                when(item.rule.alert.method)
                {
                    AlertMethod.Telegram -> TelegramAlertWriter().write(item)

                    else -> throw Exception("Unsupported alert method.")
                }
            }
        }
    }
}