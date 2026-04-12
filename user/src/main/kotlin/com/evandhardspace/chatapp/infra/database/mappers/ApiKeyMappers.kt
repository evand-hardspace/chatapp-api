package com.evandhardspace.chatapp.infra.database.mappers

import com.evandhardspace.chatapp.domain.model.ApiKey
import com.evandhardspace.chatapp.infra.database.entities.ApiKeyEntity

fun ApiKeyEntity.toApiKey(): ApiKey {
    return ApiKey(
        key = key,
        validFrom = validFrom,
        expiresAt = expiresAt,
    )
}