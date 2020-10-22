package me.aleksandarzekovic.quizbox.di.components

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import me.aleksandarzekovic.quizbox.di.QuizboxApplication
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.ViewModelFactory
import me.aleksandarzekovic.quizbox.di.modules.AppModule
import me.aleksandarzekovic.quizbox.di.modules.firebasemodule.FirebaseModule
import me.aleksandarzekovic.quizbox.di.modules.splashmodule.SplashModule
import me.aleksandarzekovic.quizbox.di.modules.userauthmodules.LoginModule

@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, LoginModule::class, ViewModelFactory::class, FirebaseModule::class, SplashModule::class])
interface AppComponent : AndroidInjector<QuizboxApplication> {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<QuizboxApplication>
}