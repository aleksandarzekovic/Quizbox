package me.aleksandarzekovic.quizbox.data.models.quizquestions

import com.google.firebase.firestore.DocumentId

data class QuizQuestionsModel(
    @DocumentId
    var questionId: String? = null,
    var question: String? = null,
    var answer: String? = null,
    var option_a: String? = null,
    var option_b: String? = null,
    var option_c: String? = null,
    var option_d: String? = null,
    var timer: Long? = null,
    var visibility: Boolean? = null
)