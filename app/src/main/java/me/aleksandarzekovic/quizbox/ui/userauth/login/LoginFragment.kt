package me.aleksandarzekovic.quizbox.ui.userauth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import me.aleksandarzekovic.quizbox.R
import me.aleksandarzekovic.quizbox.data.models.userauth.login.LoginModel
import me.aleksandarzekovic.quizbox.databinding.LoginFragmentBinding
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.DaggerAwareViewModelFactory
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class LoginFragment : DaggerFragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginFragmentBinding: LoginFragmentBinding

    @Inject
    lateinit var awareViewModelFactory: DaggerAwareViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginFragmentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this.context),
            R.layout.login_fragment,
            container,
            false
        )
        loginFragmentBinding.lifecycleOwner = this
        return loginFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loginViewModel =
            ViewModelProvider(this, awareViewModelFactory).get(LoginViewModel::class.java)
        loginFragmentBinding.loginViewModel = loginViewModel
        loginFragmentBinding.loginModel = LoginModel("", "")

        loginFragmentBinding.loginForgotPassword.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }

        loginFragmentBinding.loginRegistration.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        loginViewModel.userInfo.observe(
            viewLifecycleOwner,
            {
                when (it) {

                    is Resource.Loading -> {
                        loginFragmentBinding.loginProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        loginFragmentBinding.loginProgressBar.visibility = View.INVISIBLE
                        view?.findNavController()
                            ?.navigate(R.id.action_loginFragment_to_quizMenuFragment)
                    }

                    is Resource.Failure -> {
                        loginFragmentBinding.loginProgressBar.visibility = View.INVISIBLE
                        Snackbar.make(
                            this.requireView(),
                            "${it.throwable.message}",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            })
    }

}