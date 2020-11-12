package me.aleksandarzekovic.quizbox.data.repository.quizmenu

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDB
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDBDao
import me.aleksandarzekovic.quizbox.data.models.quizmenu.QuizTypeModel
import me.aleksandarzekovic.quizbox.utils.Resource
import me.aleksandarzekovic.quizbox.utils.service.performGetOperation
import me.aleksandarzekovic.quizbox.utils.service.toQuizType
import timber.log.Timber
import javax.inject.Inject

class QuizMenuRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val localDataSource: QuizTypeDBDao,
    private val firestore: FirebaseFirestore
) {

    fun getPreformQuizTypes(): LiveData<Resource<List<QuizTypeDB>>> {
        Timber.i("performGetOperation call")
        return performGetOperation(
            databaseQuery = { localDataSource.getQuizTypes() },
            networkCall = { getQuizTypes() },
            saveCallResult = {
                val listQuizTypeDB: List<QuizTypeDB> =
                    it.map { item -> item.toQuizType() }
                localDataSource.insertAll(listQuizTypeDB)
            }
        )
    }

    suspend fun getQuizTypes(): Resource<ArrayList<QuizTypeModel>> {
        val resultList = firestore.collection("QuizType").get().await()
        val eventList = resultList.toObjects(QuizTypeModel::class.java)
            .filter { p -> p.visibility == true }
        val arrayList = ArrayList(eventList)
        return Resource.Success(arrayList)
    }


    fun logOutUser() {
        firebaseAuth.signOut()
    }
}