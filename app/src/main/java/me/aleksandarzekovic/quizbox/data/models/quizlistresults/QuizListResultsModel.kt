package me.aleksandarzekovic.quizbox.data.models.quizlistresults

import com.google.firebase.firestore.DocumentId

class QuizListResultsModel(
    @DocumentId
    var document_id: String? = null,
    var correct_answers: String? = null,
    val total_answers: String? = null,
    val quiz_name: String? = null
)