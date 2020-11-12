package me.aleksandarzekovic.quizbox.ui.quizmenu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDB
import me.aleksandarzekovic.quizbox.data.repository.quizmenu.QuizMenuRepository
import javax.inject.Inject

class QuizMenuViewModel @Inject constructor(private val quizMenuRepository: QuizMenuRepository) :
    ViewModel() {

    val data = MutableLiveData<List<QuizTypeDB>>()

    val quizTypes = quizMenuRepository.getPreformQuizTypes()

    fun fillData(list: List<QuizTypeDB>) {
        data.value = list
    }

    fun logOut() {
        quizMenuRepository.logOutUser()
    }
}