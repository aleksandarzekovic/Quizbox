package me.aleksandarzekovic.quizbox.di.modules.userauthmodules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.ViewModelKey
import me.aleksandarzekovic.quizbox.ui.userauth.LoginFragment
import me.aleksandarzekovic.quizbox.ui.userauth.LoginViewModel

@Module
internal abstract class LoginModule {

    @ContributesAndroidInjector
    internal abstract fun loginFragment(): LoginFragment

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

}