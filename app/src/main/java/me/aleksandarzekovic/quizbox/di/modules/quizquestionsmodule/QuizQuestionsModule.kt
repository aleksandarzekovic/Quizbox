package me.aleksandarzekovic.quizbox.di.modules.quizquestionsmodule

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.ViewModelKey
import me.aleksandarzekovic.quizbox.ui.quizquestions.QuizQuestionsFragment
import me.aleksandarzekovic.quizbox.ui.quizquestions.QuizQuestionsViewModel

@Module
internal abstract class QuizQuestionsModule {

    @ContributesAndroidInjector
    internal abstract fun quizQuestionsFragment(): QuizQuestionsFragment

    @Binds
    @IntoMap
    @ViewModelKey(QuizQuestionsViewModel::class)
    abstract fun quizQuestionsViewModel(viewModel: QuizQuestionsViewModel): ViewModel

}