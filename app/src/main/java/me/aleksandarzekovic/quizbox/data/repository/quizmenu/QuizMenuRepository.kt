package me.aleksandarzekovic.quizbox.data.repository.quizmenu

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDB
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDBDao
import me.aleksandarzekovic.quizbox.data.models.quizmenu.QuizTypeModel
import me.aleksandarzekovic.quizbox.utils.Constants.ERROR
import me.aleksandarzekovic.quizbox.utils.Constants.QUIZTYPE
import me.aleksandarzekovic.quizbox.utils.NetManager
import me.aleksandarzekovic.quizbox.utils.Resource
import me.aleksandarzekovic.quizbox.utils.toQuizType
import javax.inject.Inject

class QuizMenuRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val localDataSource: QuizTypeDBDao,
    private val fireStore: FirebaseFirestore,
    private val netManager: NetManager
) {

    suspend fun fetchAndUpdate(): Resource<List<QuizTypeDB>> {
        try {
            netManager.isConnectedToInternet?.let { it ->
                if (it) {
                    val resultList = fireStore.collection(QUIZTYPE).get().await()
                    val eventList = resultList.toObjects(QuizTypeModel::class.java)
                    val eventListQuizTypeDB = eventList.map { list ->
                        list.toQuizType()
                    }.filter { p -> p.visibility == true }

                    localDataSource.insertAll(eventListQuizTypeDB)

                    return Resource.Success(eventListQuizTypeDB)
                }
                return Resource.Success(localDataSource.getQuizTypes())
            }
            return Resource.Failure(Throwable(ERROR))

        } catch (e: Exception) {
            return Resource.Failure(Throwable(ERROR))
        }
    }

    fun logOutUser() {
        firebaseAuth.signOut()
    }
}