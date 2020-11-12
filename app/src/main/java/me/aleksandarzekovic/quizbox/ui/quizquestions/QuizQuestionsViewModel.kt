package me.aleksandarzekovic.quizbox.ui.quizquestions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import me.aleksandarzekovic.quizbox.data.database.quizquestion.QuizQuestionsDB
import me.aleksandarzekovic.quizbox.data.models.quizquestions.UserAnswer
import me.aleksandarzekovic.quizbox.data.repository.quizquestions.QuizQuestionsRepository
import me.aleksandarzekovic.quizbox.utils.Resource
import timber.log.Timber
import javax.inject.Inject

class QuizQuestionsViewModel @Inject constructor(private val quizQuestionsRepository: QuizQuestionsRepository) :
    ViewModel() {

    private val _quizId = MutableLiveData<String>()
    var answer = MutableLiveData<List<UserAnswer>>()

    var questionfinished = MutableLiveData<List<QuizQuestionsDB>>()

    private val _questionsDB: LiveData<Resource<List<QuizQuestionsDB>>>
        get() {
            Timber.i("quizQuestionRepo call")
            return _quizId.switchMap {
                quizQuestionsRepository.getQuizQuestion(it)
            }
        }

    val questions = _questionsDB


    fun fetchData(quiz_id: String) {
        _quizId.value = quiz_id
    }

    fun dataUpdate(list: List<QuizQuestionsDB>) {
        Timber.i("dataUpdate call")
        questionfinished.value = list
    }


    fun onClickNext(model: QuizQuestionsDB) {
        if (questionfinished.value != null) {
            questionfinished.value = (questionfinished.value as List<QuizQuestionsDB>).drop(1)
        }
        Timber.i(questionfinished.value.toString())
    }

    fun onClickAnswerOption(userAnswer: UserAnswer, question: QuizQuestionsDB) {

        var correct: UserAnswer = UserAnswer.NO_CHECKED
        when (question.answer) {
            question.option_a -> correct = UserAnswer.OPTION_A
            question.option_b -> correct = UserAnswer.OPTION_B
            question.option_c -> correct = UserAnswer.OPTION_C
            question.option_d -> correct = UserAnswer.OPTION_D
        }
        when (userAnswer) {
            UserAnswer.OPTION_A -> if (question.option_a.toString() == question.answer.toString()) {
                answer.value = mutableListOf(UserAnswer.OPTION_A, UserAnswer.CORRECT)

            } else {
                answer.value = mutableListOf(UserAnswer.OPTION_A, UserAnswer.INCORRECT, correct)
            }
            UserAnswer.OPTION_B -> if (question.option_b.toString() == question.answer.toString()) {
                answer.value = mutableListOf(UserAnswer.OPTION_B, UserAnswer.CORRECT)
            } else {
                answer.value = mutableListOf(UserAnswer.OPTION_B, UserAnswer.INCORRECT, correct)
            }
            UserAnswer.OPTION_C -> if (question.option_c.toString() == question.answer.toString()) {
                answer.value = mutableListOf(UserAnswer.OPTION_C, UserAnswer.CORRECT)
            } else {
                answer.value = mutableListOf(UserAnswer.OPTION_C, UserAnswer.INCORRECT, correct)
            }
            UserAnswer.OPTION_D -> if (question.option_d.toString() == question.answer.toString()) {
                answer.value = mutableListOf(UserAnswer.OPTION_D, UserAnswer.CORRECT)
            } else {
                answer.value = mutableListOf(UserAnswer.OPTION_D, UserAnswer.INCORRECT, correct)
            }
            UserAnswer.NO_CHECKED -> {
                answer.value = mutableListOf(UserAnswer.NO_CHECKED, UserAnswer.INCORRECT, correct)
            }
            else -> {
            }
        }
    }
}