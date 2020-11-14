package me.aleksandarzekovic.quizbox.ui.quizresult

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import me.aleksandarzekovic.quizbox.R
import me.aleksandarzekovic.quizbox.databinding.QuizResultFragmentBinding
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.DaggerAwareViewModelFactory
import me.aleksandarzekovic.quizbox.utils.NetManager
import javax.inject.Inject

class QuizResultFragment : DaggerFragment() {

    companion object {
        fun newInstance() = QuizResultFragment()
    }

    private lateinit var viewModel: QuizResultViewModel
    private lateinit var quizResultsFragmentBinding: QuizResultFragmentBinding

    @Inject
    lateinit var awareViewModelFactory: DaggerAwareViewModelFactory

    @Inject
    lateinit var netManager: NetManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        quizResultsFragmentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.quiz_result_fragment, container, false
        )

        quizResultsFragmentBinding.lifecycleOwner = this
        return quizResultsFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, awareViewModelFactory).get(QuizResultViewModel::class.java)

        viewModel.saveResults(
            arguments?.get("correct_answers").toString().toInt(),
            arguments?.get("total_answers").toString().toInt(),
            arguments?.get("quiz_name").toString()
        )

        quizResultsFragmentBinding.totalAnswers = arguments?.get("total_answers").toString()
        quizResultsFragmentBinding.correctAnswers = arguments?.get("correct_answers").toString()
        Log.d("BackStack", findNavController().graph.toString())
        quizResultsFragmentBinding.homeButton.setOnClickListener {
            netManager.isConnectedToInternet?.let {
                if (it) {
                    //p0?.findNavController()?.navigate(R.id.action_resultQuizFragment_to_typeQuizFragment)
                } else {
                    Snackbar.make(
                        this.requireView(),
                        "Not connected to internet.",
                        Snackbar.LENGTH_SHORT
                    ).show()
//                    Toast.makeText(
//                        this.context,
//                        "Not connected to internet.",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }
        }
        quizResultsFragmentBinding.listResultButton.setOnClickListener {
            netManager.isConnectedToInternet?.let {
                if (it) {
                    findNavController().navigate(R.id.action_quizResultFragment_to_quizListResultsFragment)
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

}