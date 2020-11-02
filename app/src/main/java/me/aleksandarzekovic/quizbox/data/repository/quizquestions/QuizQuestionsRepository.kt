package me.aleksandarzekovic.quizbox.data.repository.quizquestions

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.data.database.QuizQuestions
import me.aleksandarzekovic.quizbox.data.database.QuizQuestionsDao
import me.aleksandarzekovic.quizbox.data.models.quizquestions.QuizQuestionsModel
import me.aleksandarzekovic.quizbox.utils.NetManager
import me.aleksandarzekovic.quizbox.utils.Resource
import me.aleksandarzekovic.quizbox.utils.service.performGetOperation
import javax.inject.Inject

class QuizQuestionsRepository @Inject constructor(
    var firebaseFirestore: FirebaseFirestore,
    private val localDataSource: QuizQuestionsDao,
    var netManager: NetManager
) {
    private val tenQuestionsQuiz = ArrayList<QuizQuestionsModel?>()

    fun getQuizQuewstion(quizId: String) = performGetOperation(
        databaseQuery = { localDataSource.getQuizQuestions() },
        networkCall = { getQuizQuestions(quizId) },
        saveCallResult = { localDataSource.insertAll(it) }
    )

    suspend fun getQuizQuestions(quizId: String): Resource<List<QuizQuestions>> {
        Log.d("Quiz_ID", quizId)
        netManager.isConnectedToInternet?.let {
            if (it) {
                val list =
                    firebaseFirestore.collection("QuizType").document(quizId)
                        .collection("Questions").get()
                        .await()

                val questions: List<QuizQuestions> = list.toObjects(QuizQuestions::class.java)
                Log.d("Tagg", questions.toString())
                return Resource.Success(questions)
            }
            return Resource.Failure(Throwable("Not connected to internet."))
        }
        return Resource.Failure(Throwable("Error."))


    }

    private fun pickQuestions(listQuestions: MutableList<QuizQuestionsModel>?): List<QuizQuestionsModel?> {
        tenQuestionsQuiz.clear()
        val filterListQuestions = listQuestions!!.filter { p -> p.visibility == true }
        val listQuizModels = filterListQuestions.size

        if (listQuizModels > 9) {
            (1..listQuizModels).shuffled().take(10).forEach {
                tenQuestionsQuiz.add(filterListQuestions[it - 1])
            }
        } else {
            (1..listQuizModels).shuffled().take(listQuizModels).forEach {
                tenQuestionsQuiz.add(filterListQuestions[it - 1])
            }
        }
        return tenQuestionsQuiz.toMutableList()
    }
}