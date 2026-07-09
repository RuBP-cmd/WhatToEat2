package com.rubp.whattoeat.data.local.entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock

@Entity("food_table")
data class FoodTable(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)
