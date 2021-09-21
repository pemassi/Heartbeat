package io.pemassi.heartbeat.global

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import org.springframework.web.context.ContextLoader
import kotlin.reflect.KClass

/**
 * SpringContext manage class
 *
 * This class is for non-manage Spring(non-bean) class to get bean via context.
 */
@Component
object SpringContext: ApplicationContextAware
{
    var context: ApplicationContext? = ContextLoader.getCurrentWebApplicationContext()

    fun <T : Any> getBean(beanClass: KClass<T>): T
    {
        return getBean(beanClass.java)
    }

    fun <T : Any> getBean(beanClass: Class<T>): T
    {
        return context!!.getBean<T>(beanClass)
    }

    override fun setApplicationContext(context: ApplicationContext)
    {
        this.context = context
    }
}