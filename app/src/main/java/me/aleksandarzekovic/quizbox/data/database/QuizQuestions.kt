package me.aleksandarzekovic.quizbox.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuizQuestions(

    @PrimaryKey
    @ColumnInfo(name = "questionId")
    val questionId: String,

    @ColumnInfo(name = "question")
    val question: String?,

    @ColumnInfo(name = "answer")
    val answer: String?,

    @ColumnInfo(name = "option_a")
    val option_a: String?,

    @ColumnInfo(name = "option_b")
    val option_b: String?,

    @ColumnInfo(name = "option_c")
    val option_c: String?,

    @ColumnInfo(name = "option_d")
    val option_d: String?,

    @ColumnInfo(name = "timer")
    val timer: Long?,

    @ColumnInfo(name = "visibility")
    val visibility: Boolean?

)