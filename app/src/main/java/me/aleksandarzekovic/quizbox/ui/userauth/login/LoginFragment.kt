package me.aleksandarzekovic.quizbox.ui.userauth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding

    @Inject
    lateinit var awareViewModelFactory: DaggerAwareViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(this.context),
            R.layout.login_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, awareViewModelFactory).get(LoginViewModel::class.java)
        binding.loginViewModel = viewModel
        binding.loginModel = LoginModel("", "")

        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }

        binding.registration.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        viewModel.user.observe(viewLifecycleOwner,
            {
                when (it) {
                    is Resource.Success -> {
                        Toast.makeText(this.context, "${it.data}", Toast.LENGTH_SHORT)
                            .show()
                        view?.findNavController()
                            ?.navigate(R.id.action_loginFragment_to_quizMenuFragment)
                    }

                    is Resource.Failure -> {
                        Toast.makeText(this.context, "${it.throwable.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> Toast.makeText(this.context, "Else.", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

}