package com.evandhardspace.chatapp.infra.database.mappers

import com.evandhardspace.chatapp.domain.model.User
import com.evandhardspace.chatapp.infra.database.entities.UserEntity

fun UserEntity.toUser(): User {
    return User(
        id = id!!,
        username = username,
        email = email,
        hasEmailVerified = hasVerifiedEmail
    )
}