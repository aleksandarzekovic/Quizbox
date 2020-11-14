package me.aleksandarzekovic.quizbox.data.repository.quizresult

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.data.database.quiz_result.QuizResultDB
import me.aleksandarzekovic.quizbox.data.database.quiz_result.QuizResultDBDao
import me.aleksandarzekovic.quizbox.utils.NetManager
import java.util.*
import javax.inject.Inject

class QuizResultRepository @Inject constructor(
    private var fireStore: FirebaseFirestore,
    private var firebaseAuth: FirebaseAuth,
    private val dataSource: QuizResultDBDao,
    var netManager: NetManager
) {
    private val current: Calendar = Calendar.getInstance()

    suspend fun getResults(): List<QuizResultDB> {
        return dataSource.getQuizResults()
    }

    suspend fun saveResult(quizResult: QuizResultDB) {
        dataSource.insert(quizResult)
    }

    suspend fun saveResultsRemote(quizResult: List<QuizResultDB>) {
        netManager.isConnectedToInternet?.let {
            if (it) {
                for (quiz in quizResult) {
                    fireStore.collection("Results")
                        .document("${firebaseAuth.currentUser?.email}")
                        .collection("Statistic")
                        .document("${quiz.documentId}")
                        .set(quizResult)
                        .await()
                    saveResult(quiz)
                }
            }
        }
    }
}
