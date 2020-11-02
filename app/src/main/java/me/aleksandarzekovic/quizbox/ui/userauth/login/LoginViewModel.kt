package me.aleksandarzekovic.quizbox.ui.userauth.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.aleksandarzekovic.quizbox.data.repository.userauth.login.LoginRepository
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    ViewModel() {

    var user = MutableLiveData<Resource<FirebaseUser?>>()

    fun onClickLogIn(email: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                Log.d("Taaaa", "upao")
                try {
                    val result = loginRepository.logIn(email, password)
                    user.value = result
                } catch (e: Exception) {
                    user.value = Resource.Failure(Throwable(e.message))
                }

            }
        }
    }
}