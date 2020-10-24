package me.aleksandarzekovic.quizbox.ui.userauth.resetpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import me.aleksandarzekovic.quizbox.R
import me.aleksandarzekovic.quizbox.databinding.ResetPasswordFragmentBinding
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.DaggerAwareViewModelFactory
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class ResetPasswordFragment : DaggerFragment() {

    companion object {
        fun newInstance() = ResetPasswordFragment()
    }

    private lateinit var viewModel: ResetPasswordViewModel
    private lateinit var binding: ResetPasswordFragmentBinding

    @Inject
    lateinit var awareViewModelFactory: DaggerAwareViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(this.context),
            R.layout.reset_password_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, awareViewModelFactory).get(ResetPasswordViewModel::class.java)

        binding.email = ""
        binding.respMessage = ""
        binding.resetPasswordViewModel = viewModel

        viewModel.resp.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.inputResetMail.visibility = View.INVISIBLE
                    binding.respMessage =
                        "A password reset has been sent to your mail. Please check."
                    binding.buttonSendMail.visibility = View.INVISIBLE
                    binding.buttonReturnToLogin.visibility = View.VISIBLE
                }

                is Resource.Failure -> {
                    Toast.makeText(this.context, "${it.throwable.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        })

        binding.buttonReturnToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }
    }
}