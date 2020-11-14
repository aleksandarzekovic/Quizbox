package me.aleksandarzekovic.quizbox.data.database.quizmenu

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizTypeDBDao {

    @Query("SELECT * FROM quiz_type")
    suspend fun getQuizTypes(): List<QuizTypeDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quizTypes: List<QuizTypeDB>)
}