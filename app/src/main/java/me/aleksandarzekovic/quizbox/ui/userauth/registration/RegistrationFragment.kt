package me.aleksandarzekovic.quizbox.ui.userauth.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import dagger.android.support.DaggerFragment
import me.aleksandarzekovic.quizbox.R
import me.aleksandarzekovic.quizbox.data.models.userauth.registration.RegistrationModel
import me.aleksandarzekovic.quizbox.databinding.RegistrationFragmentBinding
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.DaggerAwareViewModelFactory
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class RegistrationFragment : DaggerFragment() {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    @Inject
    lateinit var awareViewModelFactory: DaggerAwareViewModelFactory
    private lateinit var viewModel: RegistrationViewModel

    private lateinit var binding: RegistrationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(this.context),
            R.layout.registration_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, awareViewModelFactory).get(RegistrationViewModel::class.java)
        binding.registrationViewModel = viewModel
        binding.registrationModel = RegistrationModel("", "", "")

        binding.textRegistrationHaveAcc.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        binding.buttonToMenu.setOnClickListener {
            view?.findNavController()
                ?.navigate(R.id.action_registrationFragment_to_quizMenuFragment)
        }

        viewModel.result.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.inputRegistrationEmail.visibility = View.GONE
                    binding.inputRegistrationPassword.visibility = View.GONE
                    binding.inputRegistrationConfirmPassword.visibility = View.GONE
                    binding.buttonRegistration.visibility = View.GONE
                    binding.textRegistrationHaveAcc.visibility = View.GONE
                    binding.textHeader.visibility = View.GONE
                    binding.textSuccessfulRegistration.visibility = View.VISIBLE
                    binding.textMenuRegistration.visibility = View.VISIBLE
                    binding.buttonToMenu.visibility = View.VISIBLE
                }

                is Resource.Failure -> {
                    Toast.makeText(this.context, "${it.throwable.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> Toast.makeText(this.context, "Error.", Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }

}