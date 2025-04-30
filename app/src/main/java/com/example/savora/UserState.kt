package com.example.savora

data class UserState(
    val users: List<User> = emptyList(),
    val username: String = "",
    val password: String = "",
    val isAddingUser: Boolean =false,

)
