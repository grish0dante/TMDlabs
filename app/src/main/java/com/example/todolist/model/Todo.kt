package com.example.todolist.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.utils.DateSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.util.Date

@Entity(tableName = "todos") //rooms entity
@Serializable
@Parcelize
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val completed: Boolean,

    @Serializable(with = DateSerializer::class)
    val plannedDate: Date? = null,

    @Serializable(with = DateSerializer::class)
    val plannedStartTime: Date? = null,

    @Serializable(with = DateSerializer::class)
    val plannedEndTime: Date? = null
) : Parcelable