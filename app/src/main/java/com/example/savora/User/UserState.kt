package com.example.savora.User

data class UserState(
    val users: List<User> = emptyList(),
    val username: String = "",
    val password: String = "",
    val isAddingUser: Boolean =false,

    )
