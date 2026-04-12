package com.evandhardspace.chatapp.service

import com.evandhardspace.chatapp.domain.model.ApiKey
import com.evandhardspace.chatapp.infra.database.entities.ApiKeyEntity
import com.evandhardspace.chatapp.infra.database.mappers.toApiKey
import com.evandhardspace.chatapp.infra.database.repositories.ApiKeyRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class ApiKeyService(
    private val apiKeyRepository: ApiKeyRepository,
    @param:Value("\${chatapp.api-key.expires-in-days}") val expiresInDays: Long
) {

    @Transactional
    fun createKey(email: String): ApiKey {
        val key = ApiKey.Companion.generateKey()

        val now = Instant.now()
        val entity = ApiKeyEntity(
            key = key,
            email = email.trim(),
            validFrom = now,
            expiresAt = now.plus(expiresInDays, ChronoUnit.DAYS)
        )

        return apiKeyRepository.save(entity).toApiKey()
    }

    fun isValidKey(key: String): Boolean {
        return apiKeyRepository.findByIdOrNull(key) != null
    }
}