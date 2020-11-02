package me.aleksandarzekovic.quizbox.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import dagger.android.support.DaggerFragment
import me.aleksandarzekovic.quizbox.R
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.DaggerAwareViewModelFactory
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class SplashFragment : DaggerFragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: SplashViewModel

    @Inject
    lateinit var awareViewModelFactory: DaggerAwareViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, awareViewModelFactory).get(SplashViewModel::class.java)
        viewModel.user.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    if (it.data != null) {
                        Handler().postDelayed({
                            view?.findNavController()
                                ?.navigate(R.id.action_splashFragment_to_quizMenuFragment)
                        }, 2000)
                    } else {
                        Handler().postDelayed({
                            view?.findNavController()
                                ?.navigate(R.id.action_splashFragment_to_loginFragment)
                        }, 2000)
                    }
                }

                is Resource.Failure -> {
                    Toast.makeText(
                        this.context, "${it.throwable.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> Toast.makeText(context, "Else", Toast.LENGTH_SHORT).show()
            }
        })
    }

}