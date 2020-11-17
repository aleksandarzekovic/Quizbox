package me.aleksandarzekovic.quizbox.data.repository.userauth.registration

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class RegistrationRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    suspend fun registerUser(
        email: String,
        password: String,
        confirm_password: String
    ): Resource<FirebaseUser?> {
        if (password == confirm_password) {
            val regResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            return Resource.Success(regResult.user)
        }
        return Resource.Failure(Throwable("Passwords doesn't equal."))
    }
}