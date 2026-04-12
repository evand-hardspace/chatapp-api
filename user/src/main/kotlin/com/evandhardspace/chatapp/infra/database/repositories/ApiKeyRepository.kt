package com.evandhardspace.chatapp.infra.database.repositories

import com.evandhardspace.chatapp.infra.database.entities.ApiKeyEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ApiKeyRepository: JpaRepository<ApiKeyEntity, String>