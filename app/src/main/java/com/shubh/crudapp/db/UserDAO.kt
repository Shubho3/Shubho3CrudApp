package com.shubh.crudapp.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    @Insert
    suspend fun insertUser(user: User) : Long

    @Update
    suspend fun updateUser(user: User) : Int

    @Delete
    suspend fun deleteUser(user: User) : Int

    @Query("DELETE FROM user_data_table")
    suspend fun deleteAll() : Int
    @Query("SELECT * FROM user_data_table where user_email= :mail")
    suspend fun login(mail :String ) : User

    @Query("SELECT * FROM user_data_table")
    fun getAllUsers():Flow<List<User>>
}