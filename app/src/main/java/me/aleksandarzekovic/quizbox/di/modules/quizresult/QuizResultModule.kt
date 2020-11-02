package me.aleksandarzekovic.quizbox.di.modules.quizresult

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.ViewModelKey
import me.aleksandarzekovic.quizbox.ui.quizresult.QuizResultFragment
import me.aleksandarzekovic.quizbox.ui.quizresult.QuizResultViewModel

@Module
internal abstract class QuizResultModule {

    @ContributesAndroidInjector
    internal abstract fun quizResultsFragment(): QuizResultFragment

    @Binds
    @IntoMap
    @ViewModelKey(QuizResultViewModel::class)
    abstract fun quizResultsViewModel(viewModel: QuizResultViewModel): ViewModel

}