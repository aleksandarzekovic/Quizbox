package me.aleksandarzekovic.quizbox.ui.quizlistresults

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.aleksandarzekovic.quizbox.data.database.quiz_result.QuizResultDB
import me.aleksandarzekovic.quizbox.data.repository.quizlistresults.QuizListResultsRepository
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class QuizListResultsViewModel @Inject constructor(private val quizListResultsRepository: QuizListResultsRepository) :
    ViewModel() {

    var _listResults = MutableLiveData<Resource<List<QuizResultDB>>>()
    val listResults: LiveData<Resource<List<QuizResultDB>>>
        get() = _listResults


    fun listResults() {
        viewModelScope.launch {
            _listResults.value = Resource.Loading()
            withContext(Dispatchers.Main) {
                try {
                    _listResults.value = quizListResultsRepository.getListBestResults()
                } catch (e: Exception) {
                    _listResults.value = Resource.Failure(Throwable(e.message))
                }
            }
        }
    }
}
