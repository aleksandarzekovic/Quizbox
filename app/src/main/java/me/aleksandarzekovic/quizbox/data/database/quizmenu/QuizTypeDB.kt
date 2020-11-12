package me.aleksandarzekovic.quizbox.data.database.quizmenu

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_type")
data class QuizTypeDB(

    @PrimaryKey
    @ColumnInfo(name = "quiz_id")
    val quizId: String,

    val name: String?,

    val desc: String?,

    val visibility: Boolean?
)