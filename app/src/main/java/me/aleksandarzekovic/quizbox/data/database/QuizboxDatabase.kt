package me.aleksandarzekovic.quizbox.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDB
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDBDao
import me.aleksandarzekovic.quizbox.data.database.quizquestion.QuizQuestionsDB
import me.aleksandarzekovic.quizbox.data.database.quizquestion.QuizQuestionsDao

@Database(entities = [QuizQuestionsDB::class, QuizTypeDB::class], version = 2, exportSchema = false)
abstract class QuizboxDatabase : RoomDatabase() {

    abstract fun quizQuestionsDao(): QuizQuestionsDao
    abstract fun quizTypeDao(): QuizTypeDBDao

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