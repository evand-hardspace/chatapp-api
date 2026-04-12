package com.evandhardspace.chatapp.api.mappers

import com.evandhardspace.chatapp.api.dto.PictureUploadResponse
import com.evandhardspace.chatapp.domain.models.ProfilePictureUploadCredentials

fun ProfilePictureUploadCredentials.toResponse(): PictureUploadResponse {
    return PictureUploadResponse(
        uploadUrl = uploadUrl,
        publicUrl = publicUrl,
        headers = headers,
        expiresAt = expiresAt
    )
}