package me.aleksandarzekovic.quizbox.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import me.aleksandarzekovic.quizbox.di.QuizboxApplication

@Module
class AppModule {

    @Provides
    fun providesContext(application: QuizboxApplication): Context {
        return application.applicationContext
    }
}