package com.evandhardspace.chatapp.api.dto

import java.time.Instant

data class ApiKeyDto(
    val key: String,
    val validFrom: Instant,
    val expiresAt: Instant,
)