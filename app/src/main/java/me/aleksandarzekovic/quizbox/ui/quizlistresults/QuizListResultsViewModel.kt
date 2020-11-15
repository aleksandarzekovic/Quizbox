package me.aleksandarzekovic.quizbox.ui.quizlistresults

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import me.aleksandarzekovic.quizbox.data.models.quizlistresults.QuizListResultsModel
import me.aleksandarzekovic.quizbox.data.repository.quizlistresults.QuizListResultsRepository
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class QuizListResultsViewModel @Inject constructor(val quizListResultsRepository: QuizListResultsRepository) :
    ViewModel() {

    var _listResults = MutableLiveData<List<QuizListResultsModel>>()
    var test = MutableLiveData<Boolean>()
    val listResults = _listResults.switchMap {
        liveData {
            try {
                emit(Resource.Loading())
                emit(quizListResultsRepository.getOfQuestionsQuizData())
            } catch (e: Exception) {
                emit(Resource.Failure<List<QuizListResultsModel>>(e))
            }
        }
    }
//        viewModelScope.launch {
//            withContext(Dispatchers.Main) {
//                try {
//                    val result = quizListResultsRepository.getOfQuestionsQuizData()
//
//                    when (result) {
//                        is Resource.Success -> {
//                            Log.d("Lits", result.data.toString())
//                            bestScores(result.data)
//                        }
//                        else -> bestScores(null)
//                    }
//
//                } catch (e: Exception) {
//                    test.value = true
//                }
//            }
//        }

//    private fun bestScores(list: ArrayList<QuizListResultsModel>?) {
//        val distinctQuizNames = list!!.distinctBy {
//            it.quiz_name
//        }
//
//        val arrayList = ArrayList(distinctQuizNames)
//
//        list.forEach { lista ->
//
//            arrayList.find { it!!.quiz_name == lista.quiz_name && it.correct_answers!!.toInt() < lista.correct_answers!!.toInt() }
//                ?.correct_answers = lista.correct_answers
//
//        }
//        _listResults.value = arrayList
//    }
}
