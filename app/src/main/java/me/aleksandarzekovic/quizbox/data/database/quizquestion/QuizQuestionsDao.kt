package me.aleksandarzekovic.quizbox.data.database.quizquestion

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizQuestionsDao {

    @Query("SELECT * FROM questions")
    suspend fun getQuizQuestions(): List<QuizQuestionsDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<QuizQuestionsDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: QuizQuestionsDB)
}