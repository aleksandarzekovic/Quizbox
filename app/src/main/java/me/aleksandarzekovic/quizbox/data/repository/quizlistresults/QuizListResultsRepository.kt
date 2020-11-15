package me.aleksandarzekovic.quizbox.data.repository.quizlistresults

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.data.models.quizlistresults.QuizListResultsModel
import me.aleksandarzekovic.quizbox.utils.NetManager
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class QuizListResultsRepository @Inject constructor(
    var fireStore: FirebaseFirestore,
    var firebaseAuth: FirebaseAuth,
    var netManager: NetManager
) {

    suspend fun getOfQuestionsQuizData(): Resource<List<QuizListResultsModel>?> {
        netManager.isConnectedToInternet?.let {
            if (it) {
                val result =
                    fireStore.collection("Results").document("${firebaseAuth.currentUser!!.email}")
                        .collection("Statistic").get().await()
                val list = result.toObjects(QuizListResultsModel::class.java)
                val bestScore = bestScores(list)
                return Resource.Success(bestScore)
            }
            return Resource.Failure(Throwable("Not connected to internet."))
        }
        return Resource.Failure(Throwable("Error."))
    }

    private fun bestScores(list: List<QuizListResultsModel>?): List<QuizListResultsModel> {
        val distinctQuizNames = list!!.distinctBy {
            it.quiz_name
        }
        val arrayList = ArrayList(distinctQuizNames)

        list.forEach { lista ->
            arrayList.find { it!!.quiz_name == lista.quiz_name && it.correct_answers!!.toInt() < lista.correct_answers!!.toInt() }
                ?.correct_answers = lista.correct_answers

        }
        return arrayList
    }
}