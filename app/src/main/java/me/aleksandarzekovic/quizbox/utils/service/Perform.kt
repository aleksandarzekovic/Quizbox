package me.aleksandarzekovic.quizbox.utils.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import me.aleksandarzekovic.quizbox.data.database.QuizQuestions
import me.aleksandarzekovic.quizbox.data.models.quizquestions.QuizQuestionsModel
import me.aleksandarzekovic.quizbox.utils.Resource
import timber.log.Timber

fun <T, A> performGetOperation(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Resource<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        emit(Resource.Loading<T>())

        when (val responseStatus = networkCall.invoke()) {
            is Resource.Success -> saveCallResult(responseStatus.data!!)
            is Resource.Failure -> {
                emit(Resource.Failure<T>(Throwable(responseStatus.throwable.message)))
                //emitSource(source)
                Timber.i("performGetOperation emit failure network")
            }
            else -> emit(Resource.Failure<T>(Throwable("Error.")))
        }

        val source: LiveData<Resource<T>> = databaseQuery.invoke().map { Resource.Success(it) }
        emitSource(source)

    }

fun <T> Resource<List<T>>.populateQuiz(n: Int): Resource<List<T>> = let { resource ->
    when (resource) {
        is Resource.Loading -> resource
        is Resource.Success -> {
            if (resource.data.size >= n) resource.data.shuffled().take(n)
            else {
                if (resource.data.size < n) resource.data.shuffled().take(resource.data.size)
                else resource
            }
        }
        is Resource.Failure -> resource
    }

} as Resource<List<T>>

fun <T, R> LiveData<T>.switchMapRem(func: (T) -> LiveData<R>) =
    Transformations.switchMap(this, func)

fun QuizQuestionsModel.toQuizQuestions() = QuizQuestions(
    questionId = "$questionId",
    question = "$question",
    answer = "$answer",
    option_a = "$option_a",
    option_b = "$option_b",
    option_c = "$option_c",
    option_d = "$option_d",
    timer = timer,
    visibility = visibility
)