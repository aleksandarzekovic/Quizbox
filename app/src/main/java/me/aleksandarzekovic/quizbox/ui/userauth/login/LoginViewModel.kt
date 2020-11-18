package me.aleksandarzekovic.quizbox.ui.userauth.login

import androidx.lifecycle.LiveData
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

    private var _userInfo = MutableLiveData<Resource<FirebaseUser?>>()
    val userInfo: LiveData<Resource<FirebaseUser?>>
        get() = _userInfo

    fun onClickLogIn(email: String, password: String) {
        _userInfo.value = Resource.Loading()
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _userInfo.value = loginRepository.logIn(email, password)
                } catch (e: Exception) {
                    _userInfo.value = Resource.Failure(Throwable(e.message))
                }

            }
        }
    }
}