package me.aleksandarzekovic.quizbox.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.aleksandarzekovic.quizbox.data.repository.splash.SplashRepository
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val splashRepository: SplashRepository) :
    ViewModel() {

    private var _userInfo = MutableLiveData<Resource<FirebaseUser?>>()
    val userInfo: LiveData<Resource<FirebaseUser?>>
        get() = _userInfo

    fun checkUser() {
        _userInfo.value = Resource.Loading()
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _userInfo.value = splashRepository.getCurrentUser()
                } catch (e: Exception) {
                    _userInfo.value = Resource.Failure(Throwable(e.message))
                }

            }
        }
    }
}