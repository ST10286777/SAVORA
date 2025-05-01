package com.example.savora.User

import androidx.room.*

@Dao
interface UserDao {

    //Insert or update user
    @Upsert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?

//    @Query("SELECT * FROM user_table ORDER BY username ASC")
//    suspend fun getAllUsers(): List<User>

    @Delete
    suspend fun delete(user: User)
}