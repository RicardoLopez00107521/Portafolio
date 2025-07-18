package com.avanzo.avanzotestsimulator.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ranking_table")
data class RankingModel(
    @PrimaryKey(autoGenerate = true) var rankingId: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "points") val points: Float,
    //@Ignore val position: String = "",
)
{
    constructor(name: String, points: Float):
            this(0,name,points)
}
