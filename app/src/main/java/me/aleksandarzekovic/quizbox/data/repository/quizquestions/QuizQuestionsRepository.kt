package me.aleksandarzekovic.quizbox.data.repository.quizquestions

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.data.database.quizquestion.QuizQuestionsDB
import me.aleksandarzekovic.quizbox.data.database.quizquestion.QuizQuestionsDao
import me.aleksandarzekovic.quizbox.data.models.quizquestions.QuizQuestionsModel
import me.aleksandarzekovic.quizbox.utils.NetManager
import me.aleksandarzekovic.quizbox.utils.Resource
import me.aleksandarzekovic.quizbox.utils.populateQuiz
import me.aleksandarzekovic.quizbox.utils.toQuizQuestions
import javax.inject.Inject

class QuizQuestionsRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val localDataSource: QuizQuestionsDao,
    private val netManager: NetManager
) {

    suspend fun fetchAndUpdateQuestions(quizId: String): Resource<List<QuizQuestionsDB>> {
        try {
            netManager.isConnectedToInternet?.let {
                if (it) {
                    val resultList =
                        fireStore.collection("QuizType").document(quizId)
                            .collection("Questions").get()
                            .await()

                    val questions: List<QuizQuestionsModel> =
                        resultList.toObjects(QuizQuestionsModel::class.java)

                    val questionsDB = questions.map { res ->
                        res.toQuizQuestions()
                    }

                    localDataSource.insertAll(questionsDB)

                    return Resource.Success(questionsDB.populateQuiz(10))
                }
                return Resource.Success(localDataSource.getQuizQuestions().populateQuiz(10))
            }
        } catch (e: Exception) {
            return Resource.Failure(Throwable("Error."))
        }
        return Resource.Failure(Throwable("Error."))
    }
}