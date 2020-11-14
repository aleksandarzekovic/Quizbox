package me.aleksandarzekovic.quizbox.utils.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDB
import me.aleksandarzekovic.quizbox.data.database.quizquestion.QuizQuestionsDB
import me.aleksandarzekovic.quizbox.data.models.quizmenu.QuizTypeModel
import me.aleksandarzekovic.quizbox.data.models.quizquestions.QuizQuestionsModel
import me.aleksandarzekovic.quizbox.utils.Resource
import timber.log.Timber
import java.io.IOException

fun <T, A> performGetOperation(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Resource<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        try {
            emit(Resource.Loading<T>())

            when (val responseStatus = networkCall.invoke()) {
                is Resource.Success -> saveCallResult(responseStatus.data!!)
                is Resource.Failure -> {
                    emit(Resource.Failure<T>(Throwable(responseStatus.throwable.message)))
                }
                else -> emit(Resource.Failure<T>(Throwable("Error.")))
            }

            val source: LiveData<Resource<T>> = databaseQuery.invoke().map { Resource.Success(it) }
            emitSource(source)
        } catch (exception: IOException) {
            emit(Resource.Failure<T>(Throwable(exception.message)))
        }


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


fun QuizQuestionsModel.toQuizQuestions() = QuizQuestionsDB(
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

fun QuizTypeModel.toQuizType() = QuizTypeDB(
    quizId = "$quiz_id",
    name = "$name",
    desc = "$desc",
    visibility = visibility
)

fun logCoroutineInfo(msg: String) = Timber.i("Running on: [${Thread.currentThread().name}] | $msg")