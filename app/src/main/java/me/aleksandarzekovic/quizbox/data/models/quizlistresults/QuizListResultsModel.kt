package me.aleksandarzekovic.quizbox.data.models.quizlistresults

data class QuizListResultsModel(
    var documentId: Long? = null,
    var correctAnswers: Int? = null,
    var totalAnswers: Int? = null,
    var quizName: String? = null
)