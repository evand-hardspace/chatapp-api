package com.evandhardspace.chatapp.infra.message_queue

import com.evandhardspace.chatapp.domain.events.user.UserEvent
import com.evandhardspace.chatapp.service.EmailService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class NotificationUserEventListener(private val emailService: EmailService) {

    @RabbitListener(queues = [MessageQueues.NOTIFICATION_USER_EVENTS])
    fun handleUserEvent(event: UserEvent) {
        when(event) {
            is UserEvent.Created -> {
                emailService.sendVerificationEmail(
                    email = event.email,
                    username = event.username,
                    userId = event.userId,
                    token = event.verificationToken
                )
            }
            is UserEvent.RequestResendVerification -> {
                emailService.sendVerificationEmail(
                    email = event.email,
                    username = event.username,
                    userId = event.userId,
                    token = event.verificationToken
                )
            }
            is UserEvent.RequestResetPassword -> {
                emailService.sendPasswordResetEmail(
                    email = event.email,
                    username = event.username,
                    userId = event.userId,
                    token = event.passwordResetToken,
                    expiresIn = Duration.ofMinutes(event.expiresInMinutes)
                )
            }
            else -> Unit
        }
    }
}