package me.aleksandarzekovic.quizbox.ui.quizmenu

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.aleksandarzekovic.quizbox.data.models.quizmenu.QuizTypeModel
import me.aleksandarzekovic.quizbox.data.repository.quizmenu.QuizMenuRepository
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class QuizMenuViewModel @Inject constructor(private val quizMenuRepository: QuizMenuRepository) :
    ViewModel() {

    val data = MutableLiveData<ArrayList<QuizTypeModel>>()

    init {
        quizTypeResult()
    }

//    var quizTypeResult = liveData(Dispatchers.IO) {
//        emit(Resource.Loading())
//        try {
//            val eventList = quizMenuRepository.getQuizType()
//            emit(eventList)
//        } catch (e: Exception) {
//            emit(Resource.Failure<Exception>(e.cause!!))
//        }
//    }

    private fun quizTypeResult() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    val result = quizMenuRepository.getQuizType()
                    when (result) {
                        is Resource.Success -> {
                            data.value = result.data
                            Log.d("Test", result.data.toString())
                        }
                        else -> data.value = null
                    }
                } catch (e: Exception) {
                    data.value = null
                    Log.d("Test", "${e.message}")
                }
            }
        }
    }

    fun logOut() {
        quizMenuRepository.logOutUser()
    }
}