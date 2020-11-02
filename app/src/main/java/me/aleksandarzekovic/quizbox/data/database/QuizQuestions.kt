package me.aleksandarzekovic.quizbox.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

@Entity(tableName = "questions")
data class QuizQuestions(

    @PrimaryKey
    @DocumentId
    @ColumnInfo(name = "questionId")
    var questionId: String,

    @ColumnInfo(name = "question")
    var question: String?,

    @ColumnInfo(name = "answer")
    var answer: String?,

    @ColumnInfo(name = "option_a")
    var option_a: String?,

    @ColumnInfo(name = "option_b")
    var option_b: String?,

    @ColumnInfo(name = "option_c")
    var option_c: String?,

    @ColumnInfo(name = "option_d")
    var option_d: String?,

    @ColumnInfo(name = "timer")
    var timer: Long?,

    @ColumnInfo(name = "visibility")
    var visibility: Boolean?

)