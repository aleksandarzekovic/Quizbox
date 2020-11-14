package me.aleksandarzekovic.quizbox.data.repository.quizmenu

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDB
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDBDao
import me.aleksandarzekovic.quizbox.data.models.quizmenu.QuizTypeModel
import me.aleksandarzekovic.quizbox.utils.NetManager
import me.aleksandarzekovic.quizbox.utils.Resource
import me.aleksandarzekovic.quizbox.utils.service.logCoroutineInfo
import me.aleksandarzekovic.quizbox.utils.service.toQuizType
import javax.inject.Inject

class QuizMenuRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val localDataSource: QuizTypeDBDao,
    private val firestore: FirebaseFirestore,
    private var netManager: NetManager
) {

//    fun getPreformQuizTypes(): LiveData<Resource<List<QuizTypeDB>>> {
//        Timber.i("performGetOperation call")
//        return performGetOperation(
//            databaseQuery = { localDataSource.getQuizTypes() },
//            networkCall = { getQuizTypes() },
//            saveCallResult = { localDataSource.insertAll(it) }
//        )
//    }

    suspend fun fetchAndUpdate(): Resource<List<QuizTypeDB>> {
        logCoroutineInfo("fetchAndUpdate")
        netManager.isConnectedToInternet?.let {
            if (it) {
                val resultList = firestore.collection("QuizType").get().await()
                val eventList = resultList.toObjects(QuizTypeModel::class.java)
                val eventListQuizTypeDB = eventList.map { it ->
                    it.toQuizType()
                }.filter { p -> p.visibility == true }

                localDataSource.insertAll(eventListQuizTypeDB)

                return Resource.Success(eventListQuizTypeDB)
            }
            return Resource.Success(localDataSource.getQuizTypes())
        }
        return Resource.Failure(Throwable("Error."))

    }

    suspend fun getQuizTypes(): Resource<List<QuizTypeDB>> {
        val resultList = firestore.collection("QuizType").get().await()
        val eventList = resultList.toObjects(QuizTypeModel::class.java)
            .filter { p -> p.visibility == true }
        val listQuizType = eventList.map { it.toQuizType() }
        return Resource.Success(listQuizType)
    }


    fun logOutUser() {
        firebaseAuth.signOut()
    }
}