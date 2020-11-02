package me.aleksandarzekovic.quizbox.di.modules.quizmenumodule

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.ViewModelKey
import me.aleksandarzekovic.quizbox.ui.quizmenu.QuizMenuFragment
import me.aleksandarzekovic.quizbox.ui.quizmenu.QuizMenuViewModel

@Module
internal abstract class QuizMenuModule {

    @ContributesAndroidInjector
    internal abstract fun quizMenuFragment(): QuizMenuFragment

    @Binds
    @IntoMap
    @ViewModelKey(QuizMenuViewModel::class)
    abstract fun quizMenuViewModel(viewModel: QuizMenuViewModel): ViewModel

}