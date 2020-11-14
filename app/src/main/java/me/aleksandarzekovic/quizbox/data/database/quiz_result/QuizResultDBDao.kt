package me.aleksandarzekovic.quizbox.data.database.quiz_result

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizResultDBDao {

    @Query("SELECT * FROM quiz_results")
    suspend fun getQuizResults(): List<QuizResultDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quizResult: QuizResultDB)
}