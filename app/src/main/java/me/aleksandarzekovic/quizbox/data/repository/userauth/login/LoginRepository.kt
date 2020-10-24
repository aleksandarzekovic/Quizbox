package me.aleksandarzekovic.quizbox.data.repository.userauth.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class LoginRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    suspend fun logIn(email: String, password: String): Resource<FirebaseUser?> {
        val auth = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return Resource.Success(auth.user)
    }
}