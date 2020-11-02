package me.aleksandarzekovic.quizbox.ui.quizresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.aleksandarzekovic.quizbox.data.repository.quizresult.QuizResultRepository
import javax.inject.Inject

class QuizResultViewModel @Inject constructor(var quizResultRepository: QuizResultRepository) :
    ViewModel() {

    fun saveResults(correct_answers: String, total_answers: String, quiz_name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                quizResultRepository.saveResult(correct_answers, total_answers, quiz_name)
            }
        }
    }
}