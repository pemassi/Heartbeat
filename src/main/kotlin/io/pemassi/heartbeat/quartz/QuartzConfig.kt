package io.pemassi.heartbeat.quartz

import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.scheduling.quartz.SpringBeanJobFactory

@Configuration
class QuartzConfig
{
    @Bean
    fun schedulerFactoryBean(applicationContext: ApplicationContext, triggersListener: QuartzGlobalTriggersListener, jobListener: QuartzGlobalJobListener): SchedulerFactoryBean
    {
        val jobFactory = SpringBeanJobFactory()
        jobFactory.setApplicationContext(applicationContext)

        val propertiesFactoryBean = PropertiesFactoryBean()
        propertiesFactoryBean.setLocation(ClassPathResource("/quartz.properties"))
        propertiesFactoryBean.afterPropertiesSet()

        val schedulerFactoryBean = SchedulerFactoryBean()
        schedulerFactoryBean.setApplicationContext(applicationContext)
        schedulerFactoryBean.setJobFactory(jobFactory)
        schedulerFactoryBean.setGlobalTriggerListeners(triggersListener)
        schedulerFactoryBean.setGlobalJobListeners(jobListener)
        schedulerFactoryBean.setOverwriteExistingJobs(true)
        schedulerFactoryBean.setQuartzProperties(propertiesFactoryBean.`object`!!)
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true)
        schedulerFactoryBean.isAutoStartup = true

        return schedulerFactoryBean
    }
}