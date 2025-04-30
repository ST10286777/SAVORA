package com.example.savora

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(private val dao: UserDao): ViewModel()
{
    private val _loginResult = MutableStateFlow<Boolean?>(null)
    val loginResult: StateFlow<Boolean?> = _loginResult

    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state

     fun onEvent(event: UserEvent)
    {
       when(event)
       {
           is UserEvent.DeleteUser -> {
               viewModelScope.launch{
                   dao.delete(event.user)
               }
         }

         UserEvent.HideDialog -> {
             _state.update { it.copy(isAddingUser = false) }
         }

         UserEvent.ShowDialog -> {
             _state.update { it.copy(isAddingUser = true) }
         }

         is UserEvent.SetUserName -> {
             _state.update {it.copy( username = event.username)}
         }

         is UserEvent.SetPassword ->{
             _state.update {it.copy( password = event.password)}
       }
        is UserEvent.login->
             {
                 viewModelScope.launch {
                     val user = dao.login(event.username, event.password)
                     _loginResult.value = user != null
                 }
            }

         UserEvent.SaveUser -> {
             val username = _state.value.username
             val password = _state.value.password

             if(username.isBlank() || password.isBlank())
             { return
             }

             val user = User(
                 username =username,
                 password = password
             )

             viewModelScope.launch { dao.insert(user) }
             _state.update { it.copy(isAddingUser = false, username ="", password = "") }
         }

       }

    }
}