package com.avanzo.avanzotestsimulator.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.avanzo.avanzotestsimulator.database.model.UserModel

@Dao
interface UserDao {

    //Funcion para obtener todos los usuarios
    @Query("SELECT * FROM user_table")
    suspend fun getUser(): UserModel

    //Funcion para actualzar un usuario
    @Update
    suspend fun updateUser(user: UserModel)
    //Funcion para insertar un usuario
    @Insert
    suspend fun insertUser(user: UserModel)

    //Funcion para eliminar todos los usuarios
    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers()
    //Funcion para eliminar un usuario
    @Transaction
    @Delete
    suspend fun deleteUser(user: UserModel)
}