package me.aleksandarzekovic.quizbox.data.repository.quizlistresults

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.data.models.quizlistresults.QuizListResultsModel
import me.aleksandarzekovic.quizbox.utils.NetManager
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class QuizListResultsRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val netManager: NetManager
) {

    suspend fun getListBestResults(): Resource<List<QuizListResultsModel>> {
        netManager.isConnectedToInternet?.let {
            if (it) {
                val result =
                    fireStore.collection("Results").document("${firebaseAuth.currentUser!!.email}")
                        .collection("Statistic").get().await()
                val list = result.toObjects(QuizListResultsModel::class.java)
                val bestScores = filterBestScores(list)
                return Resource.Success(bestScores)
            }
            return Resource.Failure(Throwable("Not connected to internet."))
        }
        return Resource.Failure(Throwable("Error."))
    }

    private fun filterBestScores(list: List<QuizListResultsModel>?): List<QuizListResultsModel> {
        val distinctQuizNames = list!!.distinctBy {
            it.quizName
        }
        val arrayList = ArrayList(distinctQuizNames)

        list.forEach { lista ->
            arrayList.find { it!!.quizName == lista.quizName && it.correctAnswers!!.toInt() < lista.correctAnswers!!.toInt() }
                ?.correctAnswers = lista.correctAnswers
        }
        return arrayList
    }
}