package me.aleksandarzekovic.quizbox.data.repository.userauth.resetpassword

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class ResetPasswordRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    fun resetPasswordUser(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
    }
}