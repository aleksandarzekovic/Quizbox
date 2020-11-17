package me.aleksandarzekovic.quizbox.ui.userauth.login

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

    var userInfo = MutableLiveData<Resource<FirebaseUser?>>()

    fun onClickLogIn(email: String, password: String) {
        userInfo.value = Resource.Loading()
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    userInfo.value = loginRepository.logIn(email, password)
                } catch (e: Exception) {
                    userInfo.value = Resource.Failure(Throwable(e.message))
                }

            }
        }
    }
}