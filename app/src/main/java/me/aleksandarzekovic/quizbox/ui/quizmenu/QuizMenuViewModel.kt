package me.aleksandarzekovic.quizbox.ui.quizmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDB
import me.aleksandarzekovic.quizbox.data.repository.quizmenu.QuizMenuRepository
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class QuizMenuViewModel @Inject constructor(private val quizMenuRepository: QuizMenuRepository) :
    ViewModel() {

    private val _quizTypes = MutableLiveData<Resource<List<QuizTypeDB>>>()
    val quizTypes: LiveData<Resource<List<QuizTypeDB>>>
        get() = _quizTypes

    fun quizTypes() {
        viewModelScope.launch {
            _quizTypes.value = Resource.Loading()
            withContext(Dispatchers.Main) {
                try {
                    _quizTypes.value = quizMenuRepository.fetchAndUpdate()
                } catch (e: Exception) {
                    _quizTypes.value = Resource.Failure(Throwable(e.message))
                }
            }
        }
    }

    fun logOut() {
        quizMenuRepository.logOutUser()
    }
}