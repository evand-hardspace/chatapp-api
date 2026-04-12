package com.evandhardspace.chatapp.infra.mappers

import com.evandhardspace.chatapp.domain.model.DeviceToken
import com.evandhardspace.chatapp.infra.database.DeviceTokenEntity
import com.evandhardspace.chatapp.infra.database.PlatformEntity

fun DeviceToken.Platform.toPlatformEntity(): PlatformEntity {
    return when(this) {
        DeviceToken.Platform.ANDROID -> PlatformEntity.ANDROID
        DeviceToken.Platform.IOS -> PlatformEntity.IOS
    }
}

fun PlatformEntity.toPlatform(): DeviceToken.Platform {
    return when(this) {
        PlatformEntity.ANDROID -> DeviceToken.Platform.ANDROID
        PlatformEntity.IOS -> DeviceToken.Platform.IOS
    }
}