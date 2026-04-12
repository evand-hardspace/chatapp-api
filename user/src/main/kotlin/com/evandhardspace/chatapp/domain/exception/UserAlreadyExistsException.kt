package com.evandhardspace.chatapp.domain.exception

import java.lang.RuntimeException

class UserAlreadyExistsException: RuntimeException(
    "A user with this username or email already exists."
)