package me.aleksandarzekovic.quizbox.ui.quizlistresults

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import me.aleksandarzekovic.quizbox.R
import me.aleksandarzekovic.quizbox.databinding.QuizListResultsFragmentBinding
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.DaggerAwareViewModelFactory
import me.aleksandarzekovic.quizbox.utils.NetManager
import javax.inject.Inject

class QuizListResultsFragment : DaggerFragment() {

    companion object {
        fun newInstance() = QuizListResultsFragment()
    }

    private lateinit var viewModel: QuizListResultsViewModel
    private lateinit var quizListResultsFragmentBinding: QuizListResultsFragmentBinding

    @Inject
    lateinit var awareViewModelFactory: DaggerAwareViewModelFactory

    @Inject
    lateinit var netManager: NetManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        quizListResultsFragmentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.quiz_list_results_fragment,
            container,
            false
        )
        quizListResultsFragmentBinding.lifecycleOwner = this
        return quizListResultsFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, awareViewModelFactory).get(QuizListResultsViewModel::class.java)
        viewModel.res()
        quizListResultsFragmentBinding.backPressed.setOnClickListener {
            findNavController().navigate(
                R.id.action_quizListResultsFragment_to_quizMenuFragment
            )
        }
        quizListResultsFragmentBinding.quizListResultsViewModel = viewModel
    }


}