package com.evandhardspace.chatapp.api.mappers

import com.evandhardspace.chatapp.api.dto.ChatDto
import com.evandhardspace.chatapp.api.dto.ChatMessageDto
import com.evandhardspace.chatapp.api.dto.ChatParticipantDto
import com.evandhardspace.chatapp.domain.models.Chat
import com.evandhardspace.chatapp.domain.models.ChatMessage
import com.evandhardspace.chatapp.domain.models.ChatParticipant

fun Chat.toChatDto(): ChatDto {
    return ChatDto(
        id = id,
        participants = participants.map {
            it.toChatParticipantDto()
        },
        lastActivityAt = lastActivityAt,
        lastMessage = lastMessage?.toChatMessageDto(),
        creator = creator.toChatParticipantDto()
    )
}

fun ChatMessage.toChatMessageDto(): ChatMessageDto {
    return ChatMessageDto(
        id = id,
        chatId = chatId,
        content = content,
        createdAt = createdAt,
        senderId = sender.userId
    )
}

fun ChatParticipant.toChatParticipantDto(): ChatParticipantDto {
    return ChatParticipantDto(
        userId = userId,
        username = username,
        email = email,
        profilePictureUrl = profilePictureUrl
    )
}