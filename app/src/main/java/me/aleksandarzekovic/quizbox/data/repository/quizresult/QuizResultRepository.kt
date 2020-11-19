package me.aleksandarzekovic.quizbox.data.repository.quizresult

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.data.database.quiz_result.QuizResultDB
import me.aleksandarzekovic.quizbox.data.database.quiz_result.QuizResultDBDao
import me.aleksandarzekovic.quizbox.utils.NetManager
import javax.inject.Inject

class QuizResultRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val dataSource: QuizResultDBDao,
    private val netManager: NetManager
) {

    suspend fun saveResult(quizResult: QuizResultDB) {
        dataSource.insert(quizResult)

        netManager.isConnectedToInternet?.let {
            if (it) {
                val listQuizResult = dataSource.getQuizResults().map { result ->
                    result.sendRemote = true
                    result
                }
                for (quiz in listQuizResult) {
                    fireStore.collection("Results")
                        .document("${firebaseAuth.currentUser?.email}")
                        .collection("Statistic")
                        .document("${quiz.documentId}")
                        .set(quiz)
                        .await()
                    dataSource.insert(quiz)
                }
            }
        }
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
