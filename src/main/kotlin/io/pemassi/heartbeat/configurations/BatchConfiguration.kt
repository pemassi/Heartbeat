package io.pemassi.heartbeat.configurations

import io.pemassi.heartbeat.batch.processor.TestRuleProcessor
import io.pemassi.heartbeat.batch.reader.HeartBeatRuleReader
import io.pemassi.heartbeat.batch.writer.AlertRuleWriter
import io.pemassi.heartbeat.models.TestResult
import io.pemassi.heartbeat.models.rules.HeartBeatRule
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class BatchConfiguration(
        val jobBuilderFactory: JobBuilderFactory,
        val stepBuilderFactory: StepBuilderFactory,
        val heartBeatRuleReader: HeartBeatRuleReader)
{
    fun makeJob(): Job
    {
        return jobBuilderFactory.get("heartBeatJob")
                .start(heartBeatStep())
                .build()
    }

    fun heartBeatStep(): Step
    {
        return stepBuilderFactory.get("heartBeat-searchRules")
                .chunk<HeartBeatRule, TestResult>(1)
                .reader(heartBeatRuleReader)
                .processor(TestRuleProcessor())
                .writer(AlertRuleWriter())
                .build()
    }

}