package me.aleksandarzekovic.quizbox.ui.quizresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.aleksandarzekovic.quizbox.data.database.quiz_result.QuizResultDB
import me.aleksandarzekovic.quizbox.data.repository.quizresult.QuizResultRepository
import java.util.*
import javax.inject.Inject

class QuizResultViewModel @Inject constructor(var quizResultRepository: QuizResultRepository) :
    ViewModel() {

    private val current: Calendar = Calendar.getInstance()

    fun saveResults(correct_answers: Int, total_answers: Int, quiz_name: String?) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                val a = QuizResultDB(
                    current.timeInMillis,
                    quiz_name,
                    correct_answers,
                    total_answers,
                    false
                )
                quizResultRepository.saveResult(a)
            }
        }
    }
}