package com.evandhardspace.chatapp.domain.event

import com.evandhardspace.chatapp.domain.type.UserId

data class ProfilePictureUpdatedEvent(
    val userId: UserId,
    val newUrl: String?
)
