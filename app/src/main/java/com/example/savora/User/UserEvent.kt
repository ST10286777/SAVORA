package com.example.savora.User

interface UserEvent {

    object SaveUser: UserEvent
    data class SetUserName(val username: String) : UserEvent
    data class SetPassword(val password: String) : UserEvent
    object ShowDialog: UserEvent
    object HideDialog: UserEvent
    data class  DeleteUser(val user: User): UserEvent
    data class login(val username: String, val password : String) : UserEvent

}