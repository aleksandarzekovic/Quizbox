package me.aleksandarzekovic.quizbox.data.repository.splash

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class SplashRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    fun getCurrentUser(): Resource<FirebaseUser?> {
        return Resource.Success(firebaseAuth.currentUser)
    }
}