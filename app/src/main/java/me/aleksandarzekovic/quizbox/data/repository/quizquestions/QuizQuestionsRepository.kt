package me.aleksandarzekovic.quizbox.data.repository.quizquestions

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.data.database.QuizQuestions
import me.aleksandarzekovic.quizbox.data.database.QuizQuestionsDao
import me.aleksandarzekovic.quizbox.data.models.quizquestions.QuizQuestionsModel
import me.aleksandarzekovic.quizbox.utils.NetManager
import me.aleksandarzekovic.quizbox.utils.Resource
import me.aleksandarzekovic.quizbox.utils.service.performGetOperation
import me.aleksandarzekovic.quizbox.utils.service.toQuizQuestions
import timber.log.Timber
import javax.inject.Inject

class QuizQuestionsRepository @Inject constructor(
    var firebaseFirestore: FirebaseFirestore,
    private val localDataSource: QuizQuestionsDao,
    var netManager: NetManager
) {

    fun getQuizQuestion(quizId: String): LiveData<Resource<List<QuizQuestions>>> {
        Timber.i("performGetOperation call")
        return performGetOperation(
            databaseQuery = { localDataSource.getQuizQuestions() },
            networkCall = { getQuizQuestions(quizId) },
            saveCallResult = {
                val listQuizQuestions: List<QuizQuestions> =
                    it.map { item -> item.toQuizQuestions() }
                localDataSource.insertAll(listQuizQuestions)
            }
        )
    }

    suspend fun getQuizQuestions(quizId: String): Resource<List<QuizQuestionsModel>> {
        Timber.i("firestore call")
        netManager.isConnectedToInternet?.let {
            if (it) {
                val list =
                    firebaseFirestore.collection("QuizType").document(quizId)
                        .collection("Questions").get()
                        .await()

                val questions: List<QuizQuestionsModel> =
                    list.toObjects(QuizQuestionsModel::class.java)

                return Resource.Success(questions)
            }
            return Resource.Failure(Throwable("Not connected to internet."))
        }
        return Resource.Failure(Throwable("Error."))


    }
}