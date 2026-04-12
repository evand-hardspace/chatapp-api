package com.evandhardspace.chatapp.infra.database.mappers

import com.evandhardspace.chatapp.domain.model.EmailVerificationToken
import com.evandhardspace.chatapp.infra.database.entities.EmailVerificationTokenEntity

fun EmailVerificationTokenEntity.toEmailVerificationToken(): EmailVerificationToken {
    return EmailVerificationToken(
        id = id,
        token = token,
        user = user.toUser()
    )
}