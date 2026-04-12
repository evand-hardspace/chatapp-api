package com.evandhardspace.chatapp.service

import com.evandhardspace.chatapp.domain.type.UserId
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.time.Duration

@Service
class EmailService(
    private val javaMailSender: JavaMailSender,
    private val templateService: EmailTemplateService,
    @param:Value("\${chatapp.email.from}")
    private val emailFrom: String,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun sendVerificationEmail(
        email: String,
        username: String,
        userId: UserId,
        token: String
    ) {
        logger.info("Sending verification email for user $userId")

        // Same URL, but uses chatapp:// scheme which allows easier testing of deep links
        // without having to verify them with Apple/Google
        val devVerificationUrl = UriComponentsBuilder
            .fromUriString("chatapp://chatapp.evandhardspace.com/api/auth/verify")
            .queryParam("token", token)
            .build()
            .toUriString()

        val htmlContent = templateService.processTemplate(
            templateName = "emails/account-verification",
            variables = mapOf(
                "username" to username,
                "devVerificationUrl" to devVerificationUrl,
            )
        )

        sendHtmlEmail(
            to = email,
            subject = "Verify your ChatApp account",
            html = htmlContent
        )
    }

    fun sendPasswordResetEmail(
        email: String,
        username: String,
        userId: UserId,
        token: String,
        expiresIn: Duration
    ) {
        logger.info("Sending password reset email for user $userId")

        // Same URL, but uses chatapp:// scheme which allows easier testing of deep links
        // without having to verify them with Apple/Google
        val devUrl = UriComponentsBuilder
            .fromUriString("chatapp://chatapp.evandhardspace.com/api/auth/reset-password")
            .queryParam("token", token)
            .build()
            .toUriString()

        val htmlContent = templateService.processTemplate(
            templateName = "emails/reset-password",
            variables = mapOf(
                "username" to username,
                "devResetPasswordUrl" to devUrl,
                "expiresInMinutes" to expiresIn.toMinutes()
            )
        )

        sendHtmlEmail(
            to = email,
            subject = "Reset your ChatApp password",
            html = htmlContent,
        )
    }

    private fun sendHtmlEmail(
        to: String,
        subject: String,
        html: String
    ) {
        val message = javaMailSender.createMimeMessage()
        MimeMessageHelper(message, true, "UTF-8").apply {
            setFrom(emailFrom)
            setTo(to)
            setSubject(subject)
            setText(html, true)
        }

        try {
            javaMailSender.send(message)
        } catch(e: MailException) {
            logger.error("Could not send email", e)
        }
    }
}