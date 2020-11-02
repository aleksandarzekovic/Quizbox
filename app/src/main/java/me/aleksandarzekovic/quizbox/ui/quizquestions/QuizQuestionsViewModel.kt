package me.aleksandarzekovic.quizbox.ui.quizquestions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.aleksandarzekovic.quizbox.data.database.QuizQuestions
import me.aleksandarzekovic.quizbox.data.models.quizquestions.UserAnswer
import me.aleksandarzekovic.quizbox.data.repository.quizquestions.QuizQuestionsRepository
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class QuizQuestionsViewModel @Inject constructor(private val quizQuestionsRepository: QuizQuestionsRepository) :
    ViewModel() {

    var answer = MutableLiveData<List<UserAnswer>>()
    //var questions = MutableLiveData<Resource<List<QuizQuestionsModel?>>>()

    private val _questions = MutableLiveData<Resource<List<QuizQuestions>>>()

    val questions: LiveData<Resource<List<QuizQuestions>>>
        get() = _questions

    fun fetchData(quiz_id: String) {
        _questions.value = quizQuestionsRepository.getQuizQuewstion(quiz_id).value
//        viewModelScope.launch {
//            withContext(Dispatchers.Main) {
//                try {
//                    val result = quizQuestionsRepository.getQuizQuestions(quiz_id)
//                    questions.value = result
//                } catch (e: Exception) {
//                    questions.value = Resource.Failure(Throwable(e.message))
//                }
//
//            }
//        }
    }

    fun onClickNext() {
        if (_questions.value != null) {
            when (_questions.value) {
                is Resource.Success -> {
                    _questions.value = Resource.Success(
                        (_questions.value as Resource.Success<List<QuizQuestions>>).data.drop(
                            1
                        )
                    )
                }
            }
        }
    }

    fun onClickAnswerOption(userAnswer: UserAnswer, question: QuizQuestions) {

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