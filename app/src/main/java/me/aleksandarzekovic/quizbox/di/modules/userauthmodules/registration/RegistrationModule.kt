package me.aleksandarzekovic.quizbox.di.modules.userauthmodules.registration

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.ViewModelKey
import me.aleksandarzekovic.quizbox.ui.userauth.registration.RegistrationFragment
import me.aleksandarzekovic.quizbox.ui.userauth.registration.RegistrationViewModel

@Module
internal abstract class RegistrationModule {

    @ContributesAndroidInjector
    internal abstract fun registrationFragment(): RegistrationFragment

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    abstract fun registrationViewModel(viewModel: RegistrationViewModel): ViewModel

}