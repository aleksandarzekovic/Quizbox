package me.aleksandarzekovic.quizbox.di.modules.splashmodule

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.ViewModelKey
import me.aleksandarzekovic.quizbox.ui.splash.SplashFragment
import me.aleksandarzekovic.quizbox.ui.splash.SplashViewModel

@Module
internal abstract class SplashModule {

    @ContributesAndroidInjector
    internal abstract fun splashFragment(): SplashFragment

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun splashViewModel(viewModel: SplashViewModel): ViewModel

}