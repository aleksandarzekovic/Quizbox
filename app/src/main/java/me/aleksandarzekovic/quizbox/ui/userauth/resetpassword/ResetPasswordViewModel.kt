package me.aleksandarzekovic.quizbox.ui.userauth.resetpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.aleksandarzekovic.quizbox.data.repository.userauth.resetpassword.ResetPasswordRepository
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class ResetPasswordViewModel @Inject constructor(private val resetPasswordRepository: ResetPasswordRepository) :
    ViewModel() {

    private var _resetInfo = MutableLiveData<Resource<Unit>>()
    val resetInfo: LiveData<Resource<Unit>>
        get() = _resetInfo

    fun resetPassword(email: String) {
        _resetInfo.value = Resource.Loading()
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _resetInfo.value =
                        Resource.Success(resetPasswordRepository.resetPasswordUser(email))
                } catch (e: Exception) {
                    _resetInfo.value = Resource.Failure(Throwable(e.message))
                }
            }
        }
    }
}