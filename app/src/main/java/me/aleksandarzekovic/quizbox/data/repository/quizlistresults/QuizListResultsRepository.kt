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

    suspend fun getOfQuestionsQuizData(): Resource<ArrayList<QuizListResultsModel>?> {
        netManager.isConnectedToInternet?.let {
            if (it) {
                val result =
                    fireStore.collection("Results").document("${firebaseAuth.currentUser!!.email}")
                        .collection("Statistic").get().await()
                val list = result.toObjects(QuizListResultsModel::class.java)
                val arrayList = ArrayList(list)
                return Resource.Success(arrayList)
            }
            return Resource.Failure(Throwable("Not connected to internet."))
        }
        return Resource.Failure(Throwable("Error."))

    }
}