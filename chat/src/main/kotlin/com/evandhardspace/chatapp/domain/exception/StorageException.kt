package com.evandhardspace.chatapp.domain.exception

class StorageException(
    override val message: String?
): RuntimeException(message ?: "Unable to store file")