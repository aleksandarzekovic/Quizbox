package me.aleksandarzekovic.quizbox.di.modules.roommodule

import androidx.room.Room
import dagger.Module
import dagger.Provides
import me.aleksandarzekovic.quizbox.data.database.QuizQuestionsDao
import me.aleksandarzekovic.quizbox.data.database.QuizboxDatabase
import me.aleksandarzekovic.quizbox.di.QuizboxApplication
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideQuizboxADatabase(application: QuizboxApplication): QuizboxDatabase {
        return Room.databaseBuilder(application, QuizboxDatabase::class.java, "quizbox.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideQuestionsDao(appDataBase: QuizboxDatabase): QuizQuestionsDao {
        return appDataBase.quizQuestionsDao()
    }
//
//    @Singleton
//    @Provides
//    fun provideDatabase(application: QuizboxApplication) = QuizboxDatabase.getDatabase(
//        application.applicationContext
//    )
//
//    @Singleton
//    @Provides
//    fun provideQuestionsDao(db: QuizboxDatabase) = db.quizQuestionsDao()
}