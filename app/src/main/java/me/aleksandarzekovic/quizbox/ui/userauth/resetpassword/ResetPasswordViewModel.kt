package me.aleksandarzekovic.quizbox.ui.userauth.resetpassword

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

    var resp = MutableLiveData<Resource<Unit>>()

    fun resetPassword(email: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    resp.value = Resource.Success(resetPasswordRepository.resetPasswordUser(email))
                } catch (e: Exception) {
                    resp.value = Resource.Failure(Throwable(e.message))
                }

            }
        }
    }
}