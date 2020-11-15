package me.aleksandarzekovic.quizbox.ui.quizresult

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

    private val args: QuizResultFragmentArgs by navArgs()
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
            args.correctAnswers,
            args.totalAnswers,
            args.quizName
        )

        quizResultsFragmentBinding.totalAnswers = args.totalAnswers.toString()
        quizResultsFragmentBinding.correctAnswers = args.correctAnswers.toString()
        Log.d("BackStack", findNavController().graph.toString())
        quizResultsFragmentBinding.homeButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_quizResultFragment_to_quizMenuFragment)
        }
        quizResultsFragmentBinding.listResultButton.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_quizResultFragment_to_quizListResultsFragment)
        }
    }
}

