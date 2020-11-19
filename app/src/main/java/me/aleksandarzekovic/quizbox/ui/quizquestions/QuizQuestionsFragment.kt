package me.aleksandarzekovic.quizbox.ui.quizquestions

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import me.aleksandarzekovic.quizbox.R
import me.aleksandarzekovic.quizbox.data.database.quizquestion.QuizQuestionsDB
import me.aleksandarzekovic.quizbox.data.models.quizquestions.UserAnswer
import me.aleksandarzekovic.quizbox.databinding.QuizQuestionsFragmentBinding
import me.aleksandarzekovic.quizbox.di.daggerawareviewmodelfactory.DaggerAwareViewModelFactory
import me.aleksandarzekovic.quizbox.utils.NetManager
import me.aleksandarzekovic.quizbox.utils.Resource
import me.aleksandarzekovic.quizbox.utils.ViewStatus.INVISIBLE
import me.aleksandarzekovic.quizbox.utils.ViewStatus.VISIBLE
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

        viewModel.getQuestions(quizId)

        viewModel.remainingQuestions.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                val bundle = bundleOf(
                    "correct_answers" to sumCorrectAnswers,
                    "total_answers" to totalAnswer,
                    "quiz_name" to quizName
                )
                view?.findNavController()?.navigate(
                    R.id.action_quizQuestionsFragment_to_quizResultFragment,
                    bundle
                )
            } else {
                setView(it)
            }

        })

        viewModel.questions.observe(viewLifecycleOwner, {
            if (it != null) {
                when (it) {
                    is Resource.Loading -> {
                        quizQuestionsFragmentBinding.quizLoadProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        quizQuestionsFragmentBinding.quizLoadProgressBar.visibility = View.INVISIBLE
                        visibleUIElements()
                        totalAnswer = it.data.size
                        viewModel.setQuizQuestions(it.data)
                    }

                    is Resource.Failure -> {
                        quizQuestionsFragmentBinding.quizLoadProgressBar.visibility = View.INVISIBLE
                        Snackbar.make(
                            this.requireView(),
                            "${it.throwable.message}",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        })

        viewModel.answer.observe(viewLifecycleOwner, {
            uiUserAnswer(it)
        })

        quizQuestionsFragmentBinding.quizBackArrow.setOnClickListener {
            view?.findNavController()
                ?.navigate(R.id.action_quizQuestionsFragment_to_quizMenuFragment)
        }
    }

    private fun setView(listQuestions: List<QuizQuestionsDB>) {
        if (listQuestions.isNotEmpty()) {
            questionQuizModelDB = listQuestions[questionsIndex]
            disableOrEnableOptions(VISIBLE)
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
        quizQuestionsFragmentBinding.quizFourthAnswer.setBackgroundResource(R.drawable.outline_button_bg)
    }

    private fun visibleUIElements() {
        quizQuestionsFragmentBinding.quizNumberQuestion.visibility = View.VISIBLE
        quizQuestionsFragmentBinding.quizQuestion.visibility = View.VISIBLE
        quizQuestionsFragmentBinding.quizTimer.visibility = View.VISIBLE
        quizQuestionsFragmentBinding.quizFirstAnswer.visibility = View.VISIBLE
        quizQuestionsFragmentBinding.quizSecondAnswer.visibility = View.VISIBLE
        quizQuestionsFragmentBinding.quizThirdAnswer.visibility = View.VISIBLE
        quizQuestionsFragmentBinding.quizFourthAnswer.visibility = View.VISIBLE
    }

    private fun disableOrEnableOptions(status: Int) {
        if (status == INVISIBLE) {
            quizQuestionsFragmentBinding.quizFirstAnswer.isEnabled = false
            quizQuestionsFragmentBinding.quizFirstAnswer.setBackgroundResource(R.drawable.outline_grey_button_bg)
            quizQuestionsFragmentBinding.quizSecondAnswer.isEnabled = false
            quizQuestionsFragmentBinding.quizSecondAnswer.setBackgroundResource(R.drawable.outline_grey_button_bg)
            quizQuestionsFragmentBinding.quizThirdAnswer.isEnabled = false
            quizQuestionsFragmentBinding.quizThirdAnswer.setBackgroundResource(R.drawable.outline_grey_button_bg)
            quizQuestionsFragmentBinding.quizFourthAnswer.isEnabled = false
            quizQuestionsFragmentBinding.quizFourthAnswer.setBackgroundResource(R.drawable.outline_grey_button_bg)
            quizQuestionsFragmentBinding.quizNextQuestion.visibility = View.VISIBLE
        } else {
            quizQuestionsFragmentBinding.quizFirstAnswer.isEnabled = true
            quizQuestionsFragmentBinding.quizSecondAnswer.isEnabled = true
            quizQuestionsFragmentBinding.quizThirdAnswer.isEnabled = true
            quizQuestionsFragmentBinding.quizFourthAnswer.isEnabled = true
            quizQuestionsFragmentBinding.quizNextQuestion.visibility = View.INVISIBLE
        }

    }


    private fun uiUserAnswer(list: List<UserAnswer>) {
        disableOrEnableOptions(INVISIBLE)
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
                UserAnswer.OPTION_D -> quizQuestionsFragmentBinding.quizFourthAnswer.setBackgroundResource(
                    R.drawable.correct_answer_button_bg
                )
                else -> {
                    snackBarError()
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
                        UserAnswer.OPTION_D -> quizQuestionsFragmentBinding.quizFourthAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        else -> {
                            snackBarError()
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
                        UserAnswer.OPTION_D -> quizQuestionsFragmentBinding.quizFourthAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        else -> {
                            snackBarError()
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
                        UserAnswer.OPTION_D -> quizQuestionsFragmentBinding.quizFourthAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        else -> {
                            snackBarError()
                        }
                    }
                }
                UserAnswer.OPTION_D -> {
                    quizQuestionsFragmentBinding.quizFourthAnswer.setBackgroundResource(R.drawable.wrong_answer_button_bg)
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
                        UserAnswer.OPTION_D -> quizQuestionsFragmentBinding.quizFourthAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        else -> {
                            snackBarError()
                        }
                    }
                }
                UserAnswer.NO_CHECKED -> {
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
                        UserAnswer.OPTION_D -> quizQuestionsFragmentBinding.quizFourthAnswer.setBackgroundResource(
                            R.drawable.correct_answer_button_bg
                        )
                        else -> {
                            snackBarError()
                        }
                    }
                }
                else -> {
                    snackBarError()
                }
            }
        }
    }

    private fun snackBarError() {
        Snackbar.make(
            this.requireView(),
            "Error.",
            Snackbar.LENGTH_SHORT
        )
            .show()
    }

    private fun startTimer(seconds: Long?) {
        quizQuestionsFragmentBinding.quizTimer.text = seconds.toString()
        quizQuestionsFragmentBinding.quizTimerProgressBar.visibility = View.VISIBLE
        countDownTimer = object : CountDownTimer(seconds!!.times(1000), 10) {
            override fun onFinish() {
                disableOrEnableOptions(INVISIBLE)
                viewModel.onClickAnswerOption(UserAnswer.NO_CHECKED, questionQuizModelDB)
            }

            override fun onTick(p0: Long) {
                quizQuestionsFragmentBinding.quizTimer.text = (p0 / 1000).toString()
                val percent = (p0) / seconds!!.times(10)
                quizQuestionsFragmentBinding.quizTimerProgressBar.progress = percent.toInt()

            }
        }
        countDownTimer?.start()

    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

}