package me.aleksandarzekovic.quizbox.data.repository.quizresult

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.utils.NetManager
import java.util.*
import javax.inject.Inject

class QuizResultRepository @Inject constructor(
    private var fireStore: FirebaseFirestore,
    private var firebaseAuth: FirebaseAuth,
    var netManager: NetManager
) {
    private val current: Calendar = Calendar.getInstance()

    suspend fun saveResult(correct_answers: String, total_answers: String, quiz_name: String) {
        val result = hashMapOf(
            "correct_answers" to correct_answers,
            "total_answers" to total_answers,
            "quiz_name" to quiz_name
        )

        netManager.isConnectedToInternet?.let {
            if (it) {
                fireStore.collection("Results")
                    .document("${firebaseAuth.currentUser?.email}")
                    .collection("Statistic")
                    .document("${current.time}")
                    .set(result)
                    .await()
            }
        }
    }
}