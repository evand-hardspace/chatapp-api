package com.evandhardspace.chatapp.api.controllers

import com.evandhardspace.chatapp.api.dto.ApiKeyDto
import com.evandhardspace.chatapp.api.dto.CreateApiKeyRequest
import com.evandhardspace.chatapp.api.mappers.toApiKeyDto
import com.evandhardspace.chatapp.service.ApiKeyService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.Base64

@RestController
@RequestMapping("/api/auth/apiKey")
class ApiKeyController(
    private val apiKeyService: ApiKeyService,
    @param:Value("\${chatapp.api-key.admin.username}")
    private val adminUsername: String,
    @param:Value("\${chatapp.api-key.admin.password}")
    private val adminPassword: String,
) {

    @PostMapping
    fun createApiKey(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody body: CreateApiKeyRequest
    ): ApiKeyDto {
        if(!isAuthorized(authHeader)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        return apiKeyService.createKey(body.email).toApiKeyDto()
    }

    private fun isAuthorized(authHeader: String?): Boolean {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return false
        }

        return try {
            val base64Credentials = authHeader.substringAfter("Basic ")
            val credentials = String(Base64.getDecoder().decode(base64Credentials))
            val parts = credentials.split(":", limit = 2)

            if (parts.size != 2) {
                return false
            }

            val username = parts[0]
            val password = parts[1]

            username == adminUsername && password == adminPassword
        } catch (_: Exception) {
            false
        }
    }
}