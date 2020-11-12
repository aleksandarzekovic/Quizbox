package me.aleksandarzekovic.quizbox.ui.quizlistresults

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.aleksandarzekovic.quizbox.data.models.quizlistresults.QuizListResultsModel
import me.aleksandarzekovic.quizbox.data.repository.quizlistresults.QuizListResultsRepository
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class QuizListResultsViewModel @Inject constructor(val quizListResultsRepository: QuizListResultsRepository) :
    ViewModel() {

    var listResults = MutableLiveData<List<QuizListResultsModel?>?>()
    var test = MutableLiveData<Boolean>()
    fun res() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    val result = quizListResultsRepository.getOfQuestionsQuizData()

                    when (result) {
                        is Resource.Success -> {
                            Log.d("Lits", result.data.toString())
                            bestScores(result.data)
                        }
                        else -> bestScores(null)
                    }

                } catch (e: Exception) {
                    test.value = true
                }
            }
        }
    }

    private fun bestScores(list: ArrayList<QuizListResultsModel>?) {
        val distinctQuizNames = list!!.distinctBy {
            it.quiz_name
        }

        val arrayList = ArrayList(distinctQuizNames)

        list.forEach { lista ->

            arrayList.find { it!!.quiz_name == lista.quiz_name && it.correct_answers!!.toInt() < lista.correct_answers!!.toInt() }
                ?.correct_answers = lista.correct_answers

        }
        listResults.value = arrayList
    }
}
