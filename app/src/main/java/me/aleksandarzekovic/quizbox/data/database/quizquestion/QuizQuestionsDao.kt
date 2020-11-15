package me.aleksandarzekovic.quizbox.data.database.quizquestion

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizQuestionsDao {

    @Query("SELECT * FROM questions")
    fun getQuizQuestions(): LiveData<List<QuizQuestionsDB>>

    @Query("SELECT * FROM questions")
    suspend fun getQuizQuestions1(): List<QuizQuestionsDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<QuizQuestionsDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: QuizQuestionsDB)
}