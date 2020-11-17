package me.aleksandarzekovic.quizbox.ui.userauth.resetpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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

    private lateinit var resetPasswordViewModel: ResetPasswordViewModel
    private lateinit var resetPasswordFragmentBinding: ResetPasswordFragmentBinding

    @Inject
    lateinit var awareViewModelFactory: DaggerAwareViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resetPasswordFragmentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this.context),
            R.layout.reset_password_fragment,
            container,
            false
        )
        resetPasswordFragmentBinding.lifecycleOwner = this
        return resetPasswordFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resetPasswordViewModel =
            ViewModelProvider(this, awareViewModelFactory).get(ResetPasswordViewModel::class.java)

        resetPasswordFragmentBinding.email = ""
        resetPasswordFragmentBinding.respMessage = ""
        resetPasswordFragmentBinding.resetPasswordViewModel = resetPasswordViewModel

        resetPasswordViewModel.resetInfo.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    resetPasswordFragmentBinding.resetPwProgressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    resetPasswordFragmentBinding.resetPwProgressBar.visibility = View.INVISIBLE
                    resetPasswordFragmentBinding.inputResetMail.visibility = View.INVISIBLE
                    resetPasswordFragmentBinding.respMessage =
                        "A password reset has been sent to your mail. Please check."
                    resetPasswordFragmentBinding.buttonSendMail.visibility = View.INVISIBLE
                    resetPasswordFragmentBinding.buttonReturnToLogin.visibility = View.VISIBLE
                }

                is Resource.Failure -> {
                    resetPasswordFragmentBinding.resetPwProgressBar.visibility = View.INVISIBLE
                    Snackbar.make(
                        this.requireView(),
                        "${it.throwable.message}",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }

        })

        resetPasswordFragmentBinding.buttonReturnToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }
    }
}