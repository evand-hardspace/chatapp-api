package com.evandhardspace.chatapp.domain.event

import com.evandhardspace.chatapp.domain.type.ChatId
import com.evandhardspace.chatapp.domain.type.UserId

data class ChatParticipantLeftEvent(
    val chatId: ChatId,
    val userId: UserId
)
