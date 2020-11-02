package me.aleksandarzekovic.quizbox.data.repository.quizmenu

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.data.models.quizmenu.QuizTypeModel
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class QuizMenuRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun getQuizType(): Resource<ArrayList<QuizTypeModel>> {
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