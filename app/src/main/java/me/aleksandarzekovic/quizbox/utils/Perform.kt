package me.aleksandarzekovic.quizbox.utils

import me.aleksandarzekovic.quizbox.data.database.quiz_result.QuizResultDB
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDB
import me.aleksandarzekovic.quizbox.data.database.quizquestion.QuizQuestionsDB
import me.aleksandarzekovic.quizbox.data.models.quizlistresults.QuizListResultsModel
import me.aleksandarzekovic.quizbox.data.models.quizmenu.QuizTypeModel
import me.aleksandarzekovic.quizbox.data.models.quizquestions.QuizQuestionsModel

fun <T> List<T>.populateQuiz(n: Int): List<T> = let {
    if (it.size >= n) it.shuffled().take(n)
    else {
        if (it.size < n) it.shuffled().take(it.size)
        else it
    }
}


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

fun QuizListResultsModel.toQuizResultDB() = QuizResultDB(
    documentId = documentId!!,
    quizName = "$quizName",
    correctAnswers = correctAnswers,
    totalAnswers = totalAnswers,
    sendRemote = sendRemote
)


//fun logCoroutineInfo(msg: String) = Timber.i("Running on: [${Thread.currentThread().name}] | $msg")