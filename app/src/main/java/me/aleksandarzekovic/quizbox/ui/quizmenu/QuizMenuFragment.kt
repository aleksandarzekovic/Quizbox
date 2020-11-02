package me.aleksandarzekovic.quizbox.ui.quizmenu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import me.aleksandarzekovic.quizbox.R
import me.aleksandarzekovic.quizbox.data.models.quizmenu.QuizTypeModel
import me.aleksandarzekovic.quizbox.databinding.QuizMenuFragmentBinding
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.DaggerAwareViewModelFactory
import me.aleksandarzekovic.quizbox.utils.NetManager
import me.aleksandarzekovic.quizbox.utils.recyclerview.EventListener
import javax.inject.Inject

class QuizMenuFragment : DaggerFragment(), EventListener<QuizTypeModel> {

    companion object {
        fun newInstance() = QuizMenuFragment()
    }

    private lateinit var viewModel: QuizMenuViewModel
    private lateinit var bindingQuizMenuFragmentBinding: QuizMenuFragmentBinding

    @Inject
    lateinit var awareViewModelFactory: DaggerAwareViewModelFactory

    @Inject
    lateinit var netManager: NetManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingQuizMenuFragmentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this.context),
            R.layout.quiz_menu_fragment,
            container,
            false
        )
        bindingQuizMenuFragmentBinding.lifecycleOwner = this
        return bindingQuizMenuFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, awareViewModelFactory).get(QuizMenuViewModel::class.java)
        bindingQuizMenuFragmentBinding.quizMenuListener = this
        bindingQuizMenuFragmentBinding.quizMenuViewModel = viewModel
        initToolbars()
        // We use a String here, but any type that can be put in a Bundle is supported


//        val backStackEntryCount = NavHostFragment?.parentFragmentManager?.backStackEntryCount
        Log.d("BackStack", findNavController().graph.toString())
    }


    private fun initToolbars() {
        setHasOptionsMenu(true)
        bindingQuizMenuFragmentBinding.quizMenuToolbar.setOnMenuItemClickListener { item ->
            when (item!!.itemId) {
                R.id.action_menu_logout -> {
                    netManager.isConnectedToInternet?.let {
                        if (it) {
                            viewModel.logOut()
                            findNavController().navigate(R.id.action_quizMenuFragment_to_loginFragment)
                        } else {
                            Toast.makeText(
                                this.context,
                                "Not connected to internet.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
                R.id.action_menu_result -> {
                    netManager.isConnectedToInternet?.let {
                        if (it) {
                            findNavController().navigate(R.id.action_quizMenuFragment_to_quizListResultsFragment)
                        } else {
                            Toast.makeText(
                                this.context,
                                "Not connected to internet.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            }

            true
        }
        bindingQuizMenuFragmentBinding.quizMenuToolbar.inflateMenu(R.menu.main_menu)
    }

    override fun onItemClick(t: QuizTypeModel) {
        netManager.isConnectedToInternet?.let {
            if (it) {
                findNavController()
                    .navigate(
                        QuizMenuFragmentDirections.actionQuizMenuFragmentToQuizQuestionsFragment(
                            t.quiz_id.toString(),
                            t.name.toString()
                        )
                    )

            } else {
                Toast.makeText(
                    context,
                    "Not connected to internet.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

}