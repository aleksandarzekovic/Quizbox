package me.aleksandarzekovic.quizbox.data.database.quizmenu

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizTypeDBDao {

    @Query("SELECT * FROM quiz_type")
    fun getQuizTypes(): LiveData<List<QuizTypeDB>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(quizTypes: List<QuizTypeDB>)
}