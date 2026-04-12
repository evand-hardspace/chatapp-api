package com.evandhardspace.chatapp.domain.exception

import com.evandhardspace.chatapp.domain.type.ChatMessageId

class MessageNotFoundException(
    private val id: ChatMessageId
): RuntimeException(
    "Message with ID $id not found"
)