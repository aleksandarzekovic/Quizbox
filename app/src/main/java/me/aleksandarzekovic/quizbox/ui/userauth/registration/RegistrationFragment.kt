package me.aleksandarzekovic.quizbox.ui.userauth.registration

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
    private lateinit var registrationViewModel: RegistrationViewModel

    private lateinit var registrationFragmentBinding: RegistrationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registrationFragmentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this.context),
            R.layout.registration_fragment,
            container,
            false
        )
        registrationFragmentBinding.lifecycleOwner = this
        return registrationFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        registrationViewModel =
            ViewModelProvider(this, awareViewModelFactory).get(RegistrationViewModel::class.java)
        registrationFragmentBinding.registrationViewModel = registrationViewModel
        registrationFragmentBinding.registrationModel = RegistrationModel("", "", "")

        registrationFragmentBinding.registrationHaveAcc.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        registrationFragmentBinding.registrationToMenu.setOnClickListener {
            view?.findNavController()
                ?.navigate(R.id.action_registrationFragment_to_quizMenuFragment)
        }

        registrationViewModel.registerInfo.observe(viewLifecycleOwner, {
            when (it) {

                is Resource.Loading -> {
                    registrationFragmentBinding.registrationProgressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    registrationFragmentBinding.registrationProgressBar.visibility = View.INVISIBLE
                    registrationFragmentBinding.registrationEmail.visibility = View.GONE
                    registrationFragmentBinding.registrationPassword.visibility = View.GONE
                    registrationFragmentBinding.registrationConfirmPassword.visibility =
                        View.GONE
                    registrationFragmentBinding.registrationButton.visibility = View.GONE
                    registrationFragmentBinding.registrationHaveAcc.visibility = View.GONE
                    registrationFragmentBinding.registrationHeader.visibility = View.GONE
                    registrationFragmentBinding.registrationTextSuccessful.visibility = View.VISIBLE
                    registrationFragmentBinding.registrationTextMenu.visibility = View.VISIBLE
                    registrationFragmentBinding.registrationToMenu.visibility = View.VISIBLE
                }

                is Resource.Failure -> {
                    registrationFragmentBinding.registrationProgressBar.visibility = View.INVISIBLE
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