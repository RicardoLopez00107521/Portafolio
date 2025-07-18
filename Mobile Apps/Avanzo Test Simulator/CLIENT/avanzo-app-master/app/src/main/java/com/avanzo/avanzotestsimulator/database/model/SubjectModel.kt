package com.avanzo.avanzotestsimulator.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subject_table")
data class SubjectModel(
    @PrimaryKey(autoGenerate = true) var subjectId: Long = 0L,
    @ColumnInfo( name = "subjectCode") val subjectCode: String,
    @ColumnInfo( name = "name") val name: String,
    @ColumnInfo( name = "image") val image: String,
)
{
    constructor(subjectCode: String, name: String, image: String): this(0, subjectCode, name, image)
}
