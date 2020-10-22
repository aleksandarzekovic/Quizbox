package me.aleksandarzekovic.quizbox.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import me.aleksandarzekovic.quizbox.data.repository.splash.SplashRepository
import me.aleksandarzekovic.quizbox.utils.Resource
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val splashRepository: SplashRepository) :
    ViewModel() {

    var user = liveData(Dispatchers.Main) {
        try {
            var item = splashRepository.getCurrentUser()
            emit(item)
        } catch (e: Exception) {
            emit(Resource.Failure<Exception>(e.cause!!))
        }

    }
}