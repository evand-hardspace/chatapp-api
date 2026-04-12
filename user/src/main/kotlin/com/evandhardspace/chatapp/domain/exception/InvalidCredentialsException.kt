package com.evandhardspace.chatapp.domain.exception

class InvalidCredentialsException: RuntimeException(
    "The entered credentials aren't valid"
)