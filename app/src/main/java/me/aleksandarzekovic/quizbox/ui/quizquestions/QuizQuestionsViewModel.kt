package me.aleksandarzekovic.quizbox.ui.quizquestions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.aleksandarzekovic.quizbox.data.database.quizquestion.QuizQuestionsDB
import me.aleksandarzekovic.quizbox.data.models.quizquestions.UserAnswer
import me.aleksandarzekovic.quizbox.data.repository.quizquestions.QuizQuestionsRepository
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class QuizQuestionsViewModel @Inject constructor(private val quizQuestionsRepository: QuizQuestionsRepository) :
    ViewModel() {

    val answer = MutableLiveData<List<UserAnswer>>()

    private val _remainingQuestions = MutableLiveData<List<QuizQuestionsDB>>()
    val remainingQuestions: LiveData<List<QuizQuestionsDB>>
        get() = _remainingQuestions

    private val _questions = MutableLiveData<Resource<List<QuizQuestionsDB>>>()
    val questions: LiveData<Resource<List<QuizQuestionsDB>>>
        get() = _questions

    fun getQuestions(quizId: String) {
        _questions.value = Resource.Loading()
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _questions.value = quizQuestionsRepository.fetchAndUpdateQuestions(quizId)
                } catch (e: Exception) {
                    _questions.value = Resource.Failure(Throwable(e.message))
                }
            }
        }
    }

    fun setQuizQuestions(list: List<QuizQuestionsDB>) {
        _remainingQuestions.value = list
    }


    fun onClickNext(model: QuizQuestionsDB) {
        if (_remainingQuestions.value != null) {
            _remainingQuestions.value = (_remainingQuestions.value as List<QuizQuestionsDB>).drop(1)
        }
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
                answer.value = mutableListOf(UserAnswer.NO_CHECKED, UserAnswer.INCORRECT, correct)
            }
        }
    }
}