package com.evandhardspace.chatapp.domain.exception

import java.lang.RuntimeException

class EmailNotVerifiedException: RuntimeException(
    "Email is not verified"
)