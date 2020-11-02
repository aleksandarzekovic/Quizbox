package me.aleksandarzekovic.quizbox.di.modules.quizlistresults

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.ViewModelKey
import me.aleksandarzekovic.quizbox.ui.quizlistresults.QuizListResultsFragment
import me.aleksandarzekovic.quizbox.ui.quizlistresults.QuizListResultsViewModel

@Module
internal abstract class QuizListResultsModule {

    @ContributesAndroidInjector
    internal abstract fun quizListResultsFragment(): QuizListResultsFragment

    @Binds
    @IntoMap
    @ViewModelKey(QuizListResultsViewModel::class)
    abstract fun quizListResultsViewModel(viewModel: QuizListResultsViewModel): ViewModel

}