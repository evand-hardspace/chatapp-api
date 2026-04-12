package com.evandhardspace.chatapp.api.mappers

import com.evandhardspace.chatapp.api.dto.ApiKeyDto
import com.evandhardspace.chatapp.domain.model.ApiKey

fun ApiKey.toApiKeyDto(): ApiKeyDto {
    return ApiKeyDto(
        key = key,
        validFrom = validFrom,
        expiresAt = expiresAt,
    )
}