package me.aleksandarzekovic.quizbox.di.modules.roommodule

import dagger.Module
import dagger.Provides
import me.aleksandarzekovic.quizbox.data.database.QuizboxDatabase
import me.aleksandarzekovic.quizbox.di.QuizboxApplication
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideDatabase(application: QuizboxApplication) = QuizboxDatabase.getDatabase(
        application.applicationContext
    )

    @Singleton
    @Provides
    fun provideQuestionsDao(db: QuizboxDatabase) = db.quizQuestionsDao()

    @Singleton
    @Provides
    fun provideQuizTypeDao(db: QuizboxDatabase) = db.quizTypeDao()

    @Singleton
    @Provides
    fun provideQuizResultDao(db: QuizboxDatabase) = db.quizResultDao()

}