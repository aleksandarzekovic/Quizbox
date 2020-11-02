package me.aleksandarzekovic.quizbox.di.components

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import me.aleksandarzekovic.quizbox.di.QuizboxApplication
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.ViewModelFactory
import me.aleksandarzekovic.quizbox.di.modules.AppModule
import me.aleksandarzekovic.quizbox.di.modules.firebasemodule.FirebaseModule
import me.aleksandarzekovic.quizbox.di.modules.quizlistresults.QuizListResultsModule
import me.aleksandarzekovic.quizbox.di.modules.quizmenumodule.QuizMenuModule
import me.aleksandarzekovic.quizbox.di.modules.quizquestionsmodule.QuizQuestionsModule
import me.aleksandarzekovic.quizbox.di.modules.quizresult.QuizResultModule
import me.aleksandarzekovic.quizbox.di.modules.splashmodule.SplashModule
import me.aleksandarzekovic.quizbox.di.modules.userauthmodules.login.LoginModule
import me.aleksandarzekovic.quizbox.di.modules.userauthmodules.registration.RegistrationModule
import me.aleksandarzekovic.quizbox.di.modules.userauthmodules.resetpassword.ResetPasswordModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AppModule::class,
        LoginModule::class,
        ViewModelFactory::class,
        FirebaseModule::class,
        SplashModule::class,
        RegistrationModule::class,
        ResetPasswordModule::class,
        QuizMenuModule::class,
        QuizQuestionsModule::class,
        QuizResultModule::class,
        QuizListResultsModule::class]
)
interface AppComponent : AndroidInjector<QuizboxApplication> {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<QuizboxApplication>
}