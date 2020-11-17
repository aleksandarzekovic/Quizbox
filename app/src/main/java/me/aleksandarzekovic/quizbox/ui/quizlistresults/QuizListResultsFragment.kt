package me.aleksandarzekovic.quizbox.ui.quizlistresults

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
import me.aleksandarzekovic.quizbox.databinding.QuizListResultsFragmentBinding
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.DaggerAwareViewModelFactory
import me.aleksandarzekovic.quizbox.utils.NetManager
import me.aleksandarzekovic.quizbox.utils.Resource
import timber.log.Timber
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

        viewModel._listResults.value = listOf()
        val a = QuizListResultsAdapter()
        quizListResultsFragmentBinding.listResultsRecyclerView.adapter = a

        viewModel.listResults.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    a.submitList(it.data)
                }
                is Resource.Failure -> {
                    Snackbar.make(
                        requireView(),
                        "${it.throwable.message} slika",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                    Timber.i(it.throwable.message)
                }
            }
        })
        quizListResultsFragmentBinding.backPressed.setOnClickListener {
            view?.findNavController()?.navigate(
                R.id.action_quizListResultsFragment_to_quizMenuFragment
            )
        }
        quizListResultsFragmentBinding.quizListResultsViewModel = viewModel
    }


}