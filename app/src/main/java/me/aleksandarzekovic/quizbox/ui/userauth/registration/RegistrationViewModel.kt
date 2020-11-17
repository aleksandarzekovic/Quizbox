package me.aleksandarzekovic.quizbox.ui.userauth.registration

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

class RegistrationViewModel @Inject constructor(private var registrationRepository: RegistrationRepository) :
    ViewModel() {

    var registerInfo = MutableLiveData<Resource<FirebaseUser?>>()

    fun registerUser(email: String, password: String, confirmPassword: String) {
        registerInfo.value = Resource.Loading()
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    registerInfo.value =
                        registrationRepository.registerUser(email, password, confirmPassword)
                } catch (e: Exception) {
                    registerInfo.value = Resource.Failure(Throwable(e.message))
                }
            }
        }
    }
}