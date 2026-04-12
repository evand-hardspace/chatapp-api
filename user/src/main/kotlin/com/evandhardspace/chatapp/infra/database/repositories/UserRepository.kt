package com.evandhardspace.chatapp.infra.database.repositories

import com.evandhardspace.chatapp.domain.type.UserId
import com.evandhardspace.chatapp.infra.database.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, UserId> {
    fun findByEmail(email: String): UserEntity?
    fun findByEmailOrUsername(email: String, username: String): UserEntity?
}