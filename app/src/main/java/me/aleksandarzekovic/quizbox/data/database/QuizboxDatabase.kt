package me.aleksandarzekovic.quizbox.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [QuizQuestions::class], version = 1, exportSchema = false)
abstract class QuizboxDatabase : RoomDatabase() {

    abstract fun quizQuestionsDao(): QuizQuestionsDao

    companion object {
        @Volatile
        private var instance: QuizboxDatabase? = null

        fun getDatabase(context: Context): QuizboxDatabase =
            instance ?: synchronized(this)
            { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, QuizboxDatabase::class.java, "quizbox.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}