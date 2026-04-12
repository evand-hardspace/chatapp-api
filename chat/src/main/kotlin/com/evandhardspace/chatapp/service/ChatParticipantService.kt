package com.evandhardspace.chatapp.service

import com.evandhardspace.chatapp.domain.models.ChatParticipant
import com.evandhardspace.chatapp.infra.database.mappers.toChatParticipant
import com.evandhardspace.chatapp.infra.database.mappers.toChatParticipantEntity
import com.evandhardspace.chatapp.infra.database.repositories.ChatParticipantRepository
import com.evandhardspace.chatapp.domain.type.UserId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ChatParticipantService(
    private val chatParticipantRepository: ChatParticipantRepository,
) {

    fun createChatParticipant(
        chatParticipant: ChatParticipant
    ) {
        chatParticipantRepository.save(
            chatParticipant.toChatParticipantEntity()
        )
    }

    fun findChatParticipantById(userId: UserId): ChatParticipant? {
        return chatParticipantRepository.findByIdOrNull(userId)?.toChatParticipant()
    }

    fun findChatParticipantByEmailOrUsername(
        query: String
    ): ChatParticipant? {
        val normalizedQuery = query.lowercase().trim()
        return chatParticipantRepository.findByEmailOrUsername(
            query = normalizedQuery
        )?.toChatParticipant()
    }
}