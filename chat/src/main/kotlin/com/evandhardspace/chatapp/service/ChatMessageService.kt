package com.evandhardspace.chatapp.service

import com.evandhardspace.chatapp.domain.event.MessageDeletedEvent
import com.evandhardspace.chatapp.domain.events.chat.ChatEvent
import com.evandhardspace.chatapp.domain.exception.ChatNotFoundException
import com.evandhardspace.chatapp.domain.exception.ChatParticipantNotFoundException
import com.evandhardspace.chatapp.domain.exception.ForbiddenException
import com.evandhardspace.chatapp.domain.exception.MessageNotFoundException
import com.evandhardspace.chatapp.domain.models.ChatMessage
import com.evandhardspace.chatapp.domain.type.ChatId
import com.evandhardspace.chatapp.domain.type.ChatMessageId
import com.evandhardspace.chatapp.domain.type.UserId
import com.evandhardspace.chatapp.infra.database.entities.ChatMessageEntity
import com.evandhardspace.chatapp.infra.database.mappers.toChatMessage
import com.evandhardspace.chatapp.infra.database.repositories.ChatMessageRepository
import com.evandhardspace.chatapp.infra.database.repositories.ChatParticipantRepository
import com.evandhardspace.chatapp.infra.database.repositories.ChatRepository
import com.evandhardspace.chatapp.infra.message_queue.EventPublisher
import org.springframework.cache.annotation.CacheEvict
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ChatMessageService(
    private val chatRepository: ChatRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val eventPublisher: EventPublisher,
    private val messageCacheManager: MessageCacheManager
) {

    @Transactional
    @CacheEvict(
        value = ["messages"],
        key = "#chatId",
    )
    fun sendMessage(
        chatId: ChatId,
        senderId: UserId,
        content: String,
        messageId: ChatMessageId? = null
    ): ChatMessage {
        val chat = chatRepository.findChatById(chatId, senderId)
            ?: throw ChatNotFoundException()
        val sender = chatParticipantRepository.findByIdOrNull(senderId)
            ?: throw ChatParticipantNotFoundException(senderId)

        val savedMessage = chatMessageRepository.saveAndFlush(
            ChatMessageEntity(
                id = messageId ?: UUID.randomUUID(),
                content = content.trim(),
                chatId = chatId,
                chat = chat,
                sender = sender
            )
        )

        eventPublisher.publish(
            event = ChatEvent.NewMessage(
                senderId = sender.userId,
                senderUsername = sender.username,
                recipientIds = chat.participants.map { it.userId }.toSet(),
                chatId = chatId,
                message = savedMessage.content
            )
        )

        return savedMessage.toChatMessage()
    }

    @Transactional
    fun deleteMessage(
        messageId: ChatMessageId,
        requestUserId: UserId
    ) {
        val message = chatMessageRepository.findByIdOrNull(messageId)
            ?: throw MessageNotFoundException(messageId)

        if(message.sender.userId != requestUserId) {
            throw ForbiddenException()
        }

        chatMessageRepository.delete(message)

        applicationEventPublisher.publishEvent(
            MessageDeletedEvent(
                chatId = message.chatId,
                messageId = messageId
            )
        )

        messageCacheManager.evictMessagesCache(message.chatId)
    }


}

@Component
class MessageCacheManager {
    @CacheEvict(
        value = ["messages"],
        key = "#chatId",
    )
    fun evictMessagesCache(chatId: ChatId) {
        // NO-OP: Let Spring handle the cache evict
    }
}