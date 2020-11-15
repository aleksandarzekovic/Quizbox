package me.aleksandarzekovic.quizbox.data.repository.quizquestions

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.data.database.quizquestion.QuizQuestionsDB
import me.aleksandarzekovic.quizbox.data.database.quizquestion.QuizQuestionsDao
import me.aleksandarzekovic.quizbox.data.models.quizquestions.QuizQuestionsModel
import me.aleksandarzekovic.quizbox.utils.NetManager
import me.aleksandarzekovic.quizbox.utils.Resource
import me.aleksandarzekovic.quizbox.utils.service.toQuizQuestions
import timber.log.Timber
import javax.inject.Inject

class QuizQuestionsRepository @Inject constructor(
    var firebaseFirestore: FirebaseFirestore,
    private val localDataSource: QuizQuestionsDao,
    var netManager: NetManager
) {

//    fun getQuizQuestion(quizId: String): LiveData<Resource<List<QuizQuestionsDB>>> {
//        Timber.i("performGetOperation call")
//        return performGetOperation(
//            databaseQuery = { localDataSource.getQuizQuestions() },
//            networkCall = { getQuizQuestions(quizId) },
//            saveCallResult = {
//                val listQuizQuestionDBS: List<QuizQuestionsDB> =
//                    it.map { item -> item.toQuizQuestions() }
//                localDataSource.insertAll(listQuizQuestionDBS)
//            }
//        )
//    }

//    suspend fun fetchAndUpdate(): Resource<List<QuizTypeDB>> {
//        logCoroutineInfo("fetchAndUpdate")
//        netManager.isConnectedToInternet?.let {
//            if (it) {
//                val resultList = firebaseFirestore.collection("QuizType").get().await()
//                val eventList = resultList.toObjects(QuizTypeModel::class.java)
//                val eventListQuizTypeDB = eventList.map { it ->
//                    it.toQuizType()
//                }.filter { p -> p.visibility == true }
//
//                localDataSource.insertAll(eventListQuizTypeDB)
//
//                return Resource.Success(eventListQuizTypeDB)
//            }
//            return Resource.Success(localDataSource.getQuizTypes())
//        }
//        return Resource.Failure(Throwable("Error."))
//
//    }

    suspend fun fetchAndUpdateQuestions(quizId: String): Resource<List<QuizQuestionsDB>> {
        Timber.i("firestore call")
        netManager.isConnectedToInternet?.let {
            if (it) {
                val resultList =
                    firebaseFirestore.collection("QuizType").document(quizId)
                        .collection("Questions").get()
                        .await()

                val questions: List<QuizQuestionsModel> =
                    resultList.toObjects(QuizQuestionsModel::class.java)

                val questionsDB = questions.map { it ->
                    it.toQuizQuestions()
                }

                localDataSource.insertAll(questionsDB)

                return Resource.Success(questionsDB)
            }
            return Resource.Success(localDataSource.getQuizQuestions1())
        }
        return Resource.Failure(Throwable("Error."))
    }
}