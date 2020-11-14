package me.aleksandarzekovic.quizbox.ui.quizmenu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDB
import me.aleksandarzekovic.quizbox.data.repository.quizmenu.QuizMenuRepository
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class QuizMenuViewModel @Inject constructor(private val quizMenuRepository: QuizMenuRepository) :
    ViewModel() {

    private val _quizTypes = MutableLiveData<List<QuizTypeDB>>()

    fun fetch() {
        _quizTypes.value = listOf()
    }

    val quizTypes = _quizTypes.switchMap {
        liveData {
            try {
                emit(Resource.Loading())
                emit(quizMenuRepository.fetchAndUpdate())
            } catch (e: Exception) {
                emit(Resource.Failure<List<QuizTypeDB>>(Throwable(e.message)))
            }
        }
    }
//
//    fun run(){
//        viewModelScope.launch {
//            data.value = quizMenuRepository.getPreformQuizTypes()
//        }
//    }


    fun logOut() {
        quizMenuRepository.logOutUser()
    }
}