package me.aleksandarzekovic.quizbox.ui.userauth.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.aleksandarzekovic.quizbox.data.repository.userauth.registration.RegistrationRepository
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(private val registrationRepository: RegistrationRepository) :
    ViewModel() {

    private var _registerInfo = MutableLiveData<Resource<FirebaseUser?>>()
    val registerInfo: LiveData<Resource<FirebaseUser?>>
        get() = _registerInfo

    fun registerUser(email: String, password: String, confirmPassword: String) {
        _registerInfo.value = Resource.Loading()
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _registerInfo.value =
                        registrationRepository.registerUser(email, password, confirmPassword)
                } catch (e: Exception) {
                    _registerInfo.value = Resource.Failure(Throwable(e.message))
                }
            }
        }
    }
}