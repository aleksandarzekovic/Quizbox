package me.aleksandarzekovic.quizbox.data.database.quiz_result

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResultDB(
    @PrimaryKey
    @ColumnInfo(name = "document_id")
    var documentId: Long,

    @ColumnInfo(name = "quiz_name")
    val quizName: String?,

    @ColumnInfo(name = "correct_answers")
    var correctAnswers: Int?,

    @ColumnInfo(name = "total_answers")
    val totalAnswers: Int?,

    @ColumnInfo(name = "send_remote")
    var sendRemote: Boolean?
)