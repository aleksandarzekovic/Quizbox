package me.aleksandarzekovic.quizbox.ui.quizquestions

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import dagger.android.support.DaggerFragment
import me.aleksandarzekovic.quizbox.R
import me.aleksandarzekovic.quizbox.data.database.quizquestion.QuizQuestionsDB
import me.aleksandarzekovic.quizbox.data.models.quizquestions.UserAnswer
import me.aleksandarzekovic.quizbox.databinding.QuizQuestionsFragmentBinding
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.DaggerAwareViewModelFactory
import me.aleksandarzekovic.quizbox.utils.NetManager
import me.aleksandarzekovic.quizbox.utils.Resource
import timber.log.Timber
import javax.inject.Inject

class QuizQuestionsFragment : DaggerFragment() {

    companion object {
        fun newInstance() = QuizQuestionsFragment()
    }

    private lateinit var viewModel: QuizQuestionsViewModel
    private lateinit var quizQuestionsFragmentBinding: QuizQuestionsFragmentBinding
    private var sumCorrectAnswers = 0
    private var totalAnswer = 0
    private var countDownTimer: CountDownTimer? = null
    private var questionsIndex: Int = 0
    private var questionQuizModelDB: QuizQuestionsDB =
        QuizQuestionsDB("", "", "", "", "", "", "", 1, true)

    @Inject
    lateinit var netManager: NetManager

    @Inject
    lateinit var awareViewModelFactory: DaggerAwareViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        quizQuestionsFragmentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.quiz_questions_fragment, container, false
        )
        quizQuestionsFragmentBinding.lifecycleOwner = this
        return quizQuestionsFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, awareViewModelFactory).get(QuizQuestionsViewModel::class.java)
        val quizId = arguments?.get("quizId").toString()
        val quizName = arguments?.get("quiz_name").toString()

        quizQuestionsFragmentBinding.quizQuestionsViewModel = viewModel

        viewModel.fetchData(quizId)

        viewModel.questionfinished.observe(viewLifecycleOwner, {
            Timber.i("questionfinished observe")
            if (it.isEmpty()) {
                netManager.isConnectedToInternet?.let { net ->
                    if (net) {
                        val bundle = bundleOf(
                            "correct_answers" to sumCorrectAnswers,
                            "total_answers" to totalAnswer,
                            "quiz_name" to quizName
                        )
                        Timber.i(bundle.toString())
                        view?.findNavController()?.navigate(
                            R.id.action_quizQuestionsFragment_to_quizResultFragment,
                            bundle
                        )
                    } else {
                        Toast.makeText(
                            this.context,
                            "Not connected to internet.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                setView(quizName, it)
            }

        })

        viewModel.questionsDB.observe(viewLifecycleOwner, {
            Timber.i("questions observe")
            if (it != null) {
                when (it) {
                    is Resource.Success -> {
                        Timber.i("questions Resource.Success observe")
                        totalAnswer = it.data.size
                        Timber.i("questions ${totalAnswer} observe")
                        viewModel.dataUpdate(it.data)
                    }

                    is Resource.Failure -> {
                        Timber.i("questions Resource.Error observe")
                        Toast.makeText(this.context, "${it.throwable.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is Resource.Loading -> {
                        Timber.i("questions Resource.Loading observe")
                        Toast.makeText(this.context, "Loading", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })

        viewModel.answer.observe(viewLifecycleOwner, {
            uiUserAnswer(it)
        })
    }

    private fun setView(quizName: String, listQuestions: List<QuizQuestionsDB>) {
        if (listQuestions.isNotEmpty()) {
            questionQuizModelDB = listQuestions[questionsIndex]
            disableOrEnableOptions("ENABLE")
            quizQuestionsFragmentBinding.question = listQuestions[questionsIndex]
            quizQuestionsFragmentBinding.numOfQuestion =
                "${totalAnswer.minus(listQuestions.size).plus(1)} / $totalAnswer"
            countDownTimer?.cancel()
            resetButtons()
            startTimer(listQuestions[questionsIndex].timer)
        }

    }

    private fun resetButtons() {
        quizQuestionsFragmentBinding.quizFirstAnswer.setBackgroundResource(R.drawable.outline_button_bg)
        quizQuestionsFragmentBinding.quizSecondAnswer.setBackgroundResource(R.drawable.outline_button_bg)
        quizQuestionsFragmentBinding.quizThirdAnswer.setBackgroundResource(R.drawable.outline_button_bg)
        quizQuestionsFragmentBinding.quizFourAnswer.setBackgroundResource(R.drawable.outline_button_bg)
    }

    private fun disableOrEnableOptions(status: String) {
        if (status == "DISABLE") {
            quizQuestionsFragmentBinding.quizFirstAnswer.isEnabled = false
            quizQuestionsFragmentBinding.quizFirstAnswer.setBackgroundResource(R.drawable.outline_grey_button_bg)
            quizQuestionsFragmentBinding.quizSecondAnswer.isEnabled = false
            quizQuestionsFragmentBinding.quizSecondAnswer.setBackgroundResource(R.drawable.outline_grey_button_bg)
            quizQuestionsFragmentBinding.quizThirdAnswer.isEnabled = false
            quizQuestionsFragmentBinding.quizThirdAnswer.setBackgroundResource(R.drawable.outline_grey_button_bg)
            quizQuestionsFragmentBinding.quizFourAnswer.isEnabled = false
            quizQuestionsFragmentBinding.quizFourAnswer.setBackgroundResource(R.drawable.outline_grey_button_bg)
            quizQuestionsFragmentBinding.quizNextQuestion.visibility = View.VISIBLE
        } else {
            quizQuestionsFragmentBinding.quizFirstAnswer.isEnabled = true
            quizQuestionsFragmentBinding.quizSecondAnswer.isEnabled = true
            quizQuestionsFragmentBinding.quizThirdAnswer.isEnabled = true
            quizQuestionsFragmentBinding.quizFourAnswer.isEnabled = true
            quizQuestionsFragmentBinding.quizNextQuestion.visibility = View.INVISIBLE
        }

    }


    private fun uiUserAnswer(list: List<UserAnswer>) {
        disableOrEnableOptions("DISABLE")
        countDownTimer?.cancel()
        if (list[1] == UserAnswer.CORRECT) {
            when (list[0]) {
                UserAnswer.OPTION_A -> quizQuestionsFragmentBinding.quizFirstAnswer.setBackgroundResource(
                    R.drawable.correct_answer_button_bg
                )
                UserAnswer.OPTION_B -> quizQuestionsFragmentBinding.quizSecondAnswer.setBackgroundResource(
                    R.drawable.correct_answer_button_bg
                )
                UserAnswer.OPTION_C -> quizQuestionsFragmentBinding.quizThirdAnswer.setBackgroundResource(
                    R.drawable.correct_answer_button_bg
                )
                UserAnswer.OPTION_D -> quizQuestionsFragmentBinding.quizFourAnswer.setBackgroundResource(
                    R.drawable.correct_answer_button_bg
                )
                else -> {
                }
            }
            sumCorrectAnswers++
        } else {
            when (list[0]) {
                UserAnswer.OPTION_A -> {
                    quizQuestionsFragmentBinding.quizFirstAnswer.setBackgroundResource(R.drawable.wrong_answer_button_bg)
                    when (list[2]) {
                        UserAnswer.OPTION_A -> quizQuestionsFragmentBinding.quizFirstAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        UserAnswer.OPTION_B -> quizQuestionsFragmentBinding.quizSecondAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        UserAnswer.OPTION_C -> quizQuestionsFragmentBinding.quizThirdAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        UserAnswer.OPTION_D -> quizQuestionsFragmentBinding.quizFourAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        else -> {
                        }
                    }
                }
                UserAnswer.OPTION_B -> {
                    quizQuestionsFragmentBinding.quizSecondAnswer.setBackgroundResource(R.drawable.wrong_answer_button_bg)
                    when (list[2]) {
                        UserAnswer.OPTION_A -> quizQuestionsFragmentBinding.quizFirstAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        UserAnswer.OPTION_B -> quizQuestionsFragmentBinding.quizSecondAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        UserAnswer.OPTION_C -> quizQuestionsFragmentBinding.quizThirdAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        UserAnswer.OPTION_D -> quizQuestionsFragmentBinding.quizFourAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        else -> {
                        }
                    }
                }
                UserAnswer.OPTION_C -> {
                    quizQuestionsFragmentBinding.quizThirdAnswer.setBackgroundResource(R.drawable.wrong_answer_button_bg)
                    when (list[2]) {
                        UserAnswer.OPTION_A -> quizQuestionsFragmentBinding.quizFirstAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        UserAnswer.OPTION_B -> quizQuestionsFragmentBinding.quizSecondAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        UserAnswer.OPTION_C -> quizQuestionsFragmentBinding.quizThirdAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        UserAnswer.OPTION_D -> quizQuestionsFragmentBinding.quizFourAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        else -> {
                        }
                    }
                }
                UserAnswer.OPTION_D -> {
                    quizQuestionsFragmentBinding.quizFourAnswer.setBackgroundResource(R.drawable.wrong_answer_button_bg)
                    when (list[2]) {
                        UserAnswer.OPTION_A -> quizQuestionsFragmentBinding.quizFirstAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        UserAnswer.OPTION_B -> quizQuestionsFragmentBinding.quizSecondAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        UserAnswer.OPTION_C -> quizQuestionsFragmentBinding.quizThirdAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        UserAnswer.OPTION_D -> quizQuestionsFragmentBinding.quizFourAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        else -> {
                        }
                    }
                }
                UserAnswer.NO_CHECKED -> {
                    //quiz_four_answer.setBackgroundResource(R.drawable.wrong_answer_button_bg)
                    when (list[2]) {
                        UserAnswer.OPTION_A -> quizQuestionsFragmentBinding.quizFirstAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        UserAnswer.OPTION_B -> quizQuestionsFragmentBinding.quizSecondAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        UserAnswer.OPTION_C -> quizQuestionsFragmentBinding.quizThirdAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        UserAnswer.OPTION_D -> quizQuestionsFragmentBinding.quizFourAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        else -> {
                        }
                    }
                }
                else -> {
                }
            }
        }
    }


    private fun startTimer(seconds: Long?) {
        quizQuestionsFragmentBinding.quizTimer.text = seconds.toString()
        quizQuestionsFragmentBinding.progressBar.visibility = View.VISIBLE
        countDownTimer = object : CountDownTimer(seconds!!.times(1000), 10) {
            override fun onFinish() {
                disableOrEnableOptions("DISABLE")
                viewModel.onClickAnswerOption(UserAnswer.NO_CHECKED, questionQuizModelDB)
            }

            override fun onTick(p0: Long) {
                quizQuestionsFragmentBinding.quizTimer.text = (p0 / 1000).toString()
                val percent = (p0) / seconds!!.times(10)
                quizQuestionsFragmentBinding.progressBar.progress = percent.toInt()

            }
        }
        countDownTimer?.start()

    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

}