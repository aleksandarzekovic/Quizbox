package me.aleksandarzekovic.quizbox.di.modules.userauthmodules.login

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.ViewModelKey
import me.aleksandarzekovic.quizbox.ui.userauth.login.LoginFragment
import me.aleksandarzekovic.quizbox.ui.userauth.login.LoginViewModel

@Module
internal abstract class LoginModule {

    @ContributesAndroidInjector
    internal abstract fun loginFragment(): LoginFragment

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

}