package me.aleksandarzekovic.quizbox.ui.quizmenu

import android.os.Bundle
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
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDB
import me.aleksandarzekovic.quizbox.databinding.QuizMenuFragmentBinding
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.DaggerAwareViewModelFactory
import me.aleksandarzekovic.quizbox.utils.NetManager
import me.aleksandarzekovic.quizbox.utils.Resource
import timber.log.Timber
import javax.inject.Inject

class QuizMenuFragment : DaggerFragment(), QuizMenuAdapter.QuizMenuClickListener {

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
        //bindingQuizMenuFragmentBinding.quizMenuListener = this
        initToolbars()

        viewModel.fetch()

        val a = QuizMenuAdapter(this)

        bindingQuizMenuFragmentBinding.adapter = a
        viewModel.quizTypes.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    bindingQuizMenuFragmentBinding.quizMenuProgressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    Timber.i("Jedan")
                    it.let {
                        bindingQuizMenuFragmentBinding.quizMenuProgressBar.visibility = View.GONE
                        a.submitList(it.data)
                    }
                    //viewModel.fillData(it.data)
                }
                is Resource.Failure -> {
                    Snackbar.make(
                        this.requireView(),
                        "${it.throwable.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })


        Timber.i(findNavController().graph.toString())
    }


    private fun initToolbars() {
        Timber.i("test1")
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

    override fun onItemClick(f: QuizTypeDB) {
        netManager.isConnectedToInternet?.let {
            if (it) {
                findNavController()
                    .navigate(
                        QuizMenuFragmentDirections.actionQuizMenuFragmentToQuizQuestionsFragment(
                            f.quizId,
                            f.name.toString()
                        )
                    )

            } else {
                Snackbar.make(
                    this.requireView(),
                    "Not connected to internet.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        }
    }

//    override fun onItemClick(f: QuizTypeDB) {
//
//    }

}