package me.aleksandarzekovic.quizbox.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizQuestionsDao {

    @Query("SELECT * FROM questions")
    fun getQuizQuestions(): LiveData<List<QuizQuestions>>

    @Query("SELECT * FROM questions")
    fun getQuizQuestions1(): List<QuizQuestions>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<QuizQuestions>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: QuizQuestions)
}