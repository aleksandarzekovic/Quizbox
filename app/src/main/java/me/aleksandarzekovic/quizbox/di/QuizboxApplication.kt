package me.aleksandarzekovic.quizbox.di

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import me.aleksandarzekovic.quizbox.BuildConfig
import me.aleksandarzekovic.quizbox.di.components.DaggerAppComponent
import timber.log.Timber

class QuizboxApplication : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

}