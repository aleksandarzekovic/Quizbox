package me.aleksandarzekovic.quizbox.di.modules.userauthmodules.resetpassword

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.ViewModelKey
import me.aleksandarzekovic.quizbox.ui.userauth.resetpassword.ResetPasswordFragment
import me.aleksandarzekovic.quizbox.ui.userauth.resetpassword.ResetPasswordViewModel

@Module
internal abstract class ResetPasswordModule {

    @ContributesAndroidInjector
    internal abstract fun resetPasswordFragment(): ResetPasswordFragment

    @Binds
    @IntoMap
    @ViewModelKey(ResetPasswordViewModel::class)
    abstract fun resetPasswordViewModel(viewModel: ResetPasswordViewModel): ViewModel

}