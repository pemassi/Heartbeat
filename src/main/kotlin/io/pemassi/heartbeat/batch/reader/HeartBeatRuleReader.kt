package io.pemassi.heartbeat.batch.reader

import io.pemassi.kotlin.extensions.slf4j.getLogger
import io.pemassi.heartbeat.configurations.RuleConfiguration
import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.util.YamlParser
import org.springframework.batch.item.ItemReader
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import java.io.File
import java.util.*
import kotlin.system.exitProcess

class HeartBeatRuleReader(
        applicationContext: ConfigurableApplicationContext,
        ruleConfiguration: RuleConfiguration): ItemReader<HeartBeatRule>
{
    private val logger by getLogger()

    private val queue: Queue<HeartBeatRule> = LinkedList<HeartBeatRule>()


    init
    {
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