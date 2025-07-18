package com.avanzo.avanzotestsimulator.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.avanzo.avanzotestsimulator.database.model.SubjectModel


@Dao
interface SubjectDao {

    @Query("SELECT * FROM subject_table")
    suspend fun getAllSubjects(): List<SubjectModel>

    @Query("DELETE FROM subject_table")
    suspend fun deleteAllSubjects()

    @Insert
    suspend fun insertSubject(subject: List<SubjectModel>)
}
