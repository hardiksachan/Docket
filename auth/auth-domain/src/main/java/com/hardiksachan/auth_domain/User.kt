package com.hardiksachan.auth_domain

import com.hardiksachan.core.UniqueID

data class User(
    val id: UniqueID,
    val emailAddress: EmailAddress
)