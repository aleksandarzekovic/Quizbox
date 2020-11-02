package me.aleksandarzekovic.quizbox.utils.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import me.aleksandarzekovic.quizbox.utils.Resource

fun <T, A> performGetOperation(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Resource<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        emit(Resource.Loading<T>())
        val source: LiveData<Resource<T>> = databaseQuery.invoke().map { Resource.Success(it) }
        emitSource(source)


        when (val responseStatus = networkCall.invoke()) {
            is Resource.Success -> saveCallResult(responseStatus.data!!)
            is Resource.Failure -> {
                emit(Resource.Failure<T>(Throwable(responseStatus.throwable.message)))
                emitSource(source)
            }
            else -> emit(Resource.Failure<T>(Throwable("Error.")))
        }

    }