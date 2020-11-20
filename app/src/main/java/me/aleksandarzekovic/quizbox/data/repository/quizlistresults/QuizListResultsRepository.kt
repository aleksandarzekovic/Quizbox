package me.aleksandarzekovic.quizbox.data.repository.quizlistresults

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.data.database.quiz_result.QuizResultDB
import me.aleksandarzekovic.quizbox.data.database.quiz_result.QuizResultDBDao
import me.aleksandarzekovic.quizbox.data.models.quizlistresults.QuizListResultsModel
import me.aleksandarzekovic.quizbox.utils.Constants.ERROR
import me.aleksandarzekovic.quizbox.utils.Constants.RESULTS
import me.aleksandarzekovic.quizbox.utils.Constants.STATISTIC
import me.aleksandarzekovic.quizbox.utils.NetManager
import me.aleksandarzekovic.quizbox.utils.Resource
import me.aleksandarzekovic.quizbox.utils.toQuizResultDB
import javax.inject.Inject

class QuizListResultsRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val dataSource: QuizResultDBDao,
    private val netManager: NetManager
) {

    suspend fun getListBestResults(): Resource<List<QuizResultDB>> {
        netManager.isConnectedToInternet?.let {
            if (it) {
                val result =
                    fireStore.collection(RESULTS).document("${firebaseAuth.currentUser!!.email}")
                        .collection(STATISTIC).get().await()
                val list = result.toObjects(QuizListResultsModel::class.java)


                val quizResultDB = list.map { res ->
                    res.toQuizResultDB()
                }

                val bestScores = filterBestScores(quizResultDB)

                return Resource.Success(bestScores)
            }
            return Resource.Success(filterBestScores(dataSource.getQuizResults()))
        }
        return Resource.Failure(Throwable(ERROR))
    }

    private fun filterBestScores(list: List<QuizResultDB>?): List<QuizResultDB> {
        val distinctQuizNames = list!!.distinctBy {
            it.quizName
        }

        list.forEach {
            distinctQuizNames.find { mod -> mod.quizName == mod.quizName && mod.correctAnswers!!.toInt() < mod.correctAnswers!!.toInt() }
                ?.correctAnswers = it.correctAnswers
        }
        return distinctQuizNames
    }
}