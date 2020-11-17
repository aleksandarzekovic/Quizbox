package me.aleksandarzekovic.quizbox.ui.quizmenu

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDB
import me.aleksandarzekovic.quizbox.data.repository.quizmenu.QuizMenuRepository
import me.aleksandarzekovic.quizbox.utils.Resource
import timber.log.Timber
import javax.inject.Inject

class QuizMenuViewModel @Inject constructor(private val quizMenuRepository: QuizMenuRepository) :
    ViewModel() {

    private val _quizTypes = MutableLiveData<List<QuizTypeDB>>()

    fun fetch() {
        _quizTypes.value = listOf()
    }

    val quizTypes = _quizTypes.switchMap {
        liveData {
            emit(Resource.Loading())
            try {
                viewModelScope.launch {
                    val result = async { quizMenuRepository.fetchAndUpdate() }
                }

                emit(quizMenuRepository.fetchAndUpdate())
            } catch (e: Exception) {
                Timber.i(Throwable("ViewModel"))
                Timber.i(Log.getStackTraceString(e))
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