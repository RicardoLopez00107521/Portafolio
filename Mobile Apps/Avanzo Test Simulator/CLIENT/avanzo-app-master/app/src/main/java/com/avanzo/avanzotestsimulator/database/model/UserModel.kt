package com.avanzo.avanzotestsimulator.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "user_table")
data class UserModel(
    @PrimaryKey(autoGenerate = true) var userId: Long = 0L,
    @ColumnInfo( name = "firstname") val firstname: String,
    @ColumnInfo( name = "lastname") val lastname: String,
    //@ColumnInfo(name = "institution") val institution: String?,
    @ColumnInfo( name = "email") val email: String,
    @ColumnInfo( name = "points") val points: String,
)
{
    constructor(firstname: String, lastname: String, email: String, points: String):
            this(0, firstname, lastname, email, points)
}