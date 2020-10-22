package me.aleksandarzekovic.quizbox.di

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import me.aleksandarzekovic.quizbox.di.components.DaggerAppComponent

class QuizboxApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

}