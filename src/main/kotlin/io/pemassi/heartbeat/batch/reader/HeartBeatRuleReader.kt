package io.pemassi.heartbeat.batch.reader

import com.telcuon.appcard.restful.extension.getLogger
import io.pemassi.heartbeat.configurations.RuleConfiguration
import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.util.YamlParser
import org.springframework.batch.item.ItemReader
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Component
import java.io.File
import java.util.*
import kotlin.system.exitProcess

@Component
class HeartBeatRuleReader(
        applicationContext: ConfigurableApplicationContext,
        ruleConfiguration: RuleConfiguration): ItemReader<HeartBeatRule>
{
    private val logger by getLogger()

    private val queue: Queue<HeartBeatRule> = LinkedList<HeartBeatRule>()


    init
    {
//        val testHeartBeatRule = HeartBeatRule(
//                name = "Test Rule",
//                description = "This is test rule.",
//                test = TestRule(
//                        method = TestMethod.PING,
//                        ip = "127.0.0.0",
//                        timeout = 1000
//                ),
//                alert = AlertRule(
//                        method = AlertMethod.Telegram,
//                        botId = "879307233:AAFMWvH4FuzwyPr5c9lZsqraibN7-M7Cphk",
//                        chatId = -318415218
//                )
//        )
//
//        queue.add(testHeartBeatRule)


        run {
            val folder = File(ruleConfiguration.location)

            if(!folder.exists())
            {
                logger.error("Rule location(${folder.absolutePath}) is not exist!!")
                exitProcess(SpringApplication.exit(applicationContext))
            }

            if(!folder.isDirectory)
            {
                logger.error("Rule location(${folder.absolutePath}) is not folder!!")
                SpringApplication.exit(applicationContext)
                exitProcess(SpringApplication.exit(applicationContext))
            }

            folder.listFiles()?.let {
                for(file in it)
                {
                    if(file.isFile && file.extension == "yaml")
                    {
                        queue.add(YamlParser.parse(file, HeartBeatRule.serializer()))
                    }
                }
            }
        }
    }

    override fun read(): HeartBeatRule?
    {
        return queue.poll()
    }
}