package io.pemassi.heartbeat.models.rules.alert.details

import io.pemassi.heartbeat.models.rules.alert.AlertMethod
import io.pemassi.heartbeat.models.rules.test.TestLog
import io.pemassi.kotlin.extensions.slf4j.getLogger
import kotlinx.serialization.Serializable
import org.hibernate.validator.constraints.Range
import org.hibernate.validator.constraints.URL
import org.springframework.context.ApplicationContext
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.validation.constraints.Email


@Serializable
data class AlertEmail(
    @Email
    val from: String,
    @Email
    val to: List<String>,
    @URL
    val host: String,
    @Range(min = 1, max = 65535)
    val port: Int,
    val ssl: Boolean = false,
    val auth: Boolean = false,
    val username: String? = null,
    val password: String? = null,
    val additionalProperties: HashMap<String, String>? = null
): AlertDetail()
{
    override val method: AlertMethod
        get() = AlertMethod.Email

    override fun validation() {
        if(auth)
            require(username != null && password != null) {"When auth is true, username and password are required."}

        require(to.isNotEmpty()) {"At least one recipient is required."}
    }

    override fun reportConditionMet(testLog: TestLog, context: ApplicationContext) {
        report(testLog, context)
    }

    override fun reportRecovered(testLog: TestLog, context: ApplicationContext) {
        report(testLog, context)
    }

    private fun report(testLog: TestLog, context: ApplicationContext)
    {
        //Set default properties
        val properties = System.getProperties()
        properties["mail.smtp.host"] = host
        properties["mail.smtp.port"] = port
        properties["mail.smtp.ssl.enable"] = ssl
        properties["mail.smtp.auth"] = auth

        //Set additional properties
        additionalProperties?.let {
            for (item in it)
            {
                properties[item.key] = item.value
            }
        }

        // Get the Session object and pass username and password
        val session: Session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        }).also {
            it.debug = true
        }

        val message = MimeMessage(session)
        message.setFrom(InternetAddress(from))
        message.addRecipients(Message.RecipientType.TO, to.map { InternetAddress(it) }.toTypedArray())
        message.subject = testLog.buildAlertTitle()
        message.setText(testLog.buildAlertBody())

        logger.debug("Try to sending e-mail...")

        // Send message
        Transport.send(message)

        logger.debug("Sent message successfully....")
    }

    companion object
    {
        private val logger by getLogger()
    }

}