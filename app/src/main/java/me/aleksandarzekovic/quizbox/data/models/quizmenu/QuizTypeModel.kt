package me.aleksandarzekovic.quizbox.data.models.quizmenu

import com.google.firebase.firestore.DocumentId

data class QuizTypeModel(
    @DocumentId
    var quiz_id: String? = null,
    var name: String? = null,
    var image: String? = null,
    var desc: String? = null,
    var visibility: Boolean? = null
)

//@BindingAdapter("avatar")
//fun loadImage(imageView: ImageView, imageURL: String) {
//    Glide.with(imageView.context)
//        .load(imageURL)
//        .placeholder(R.drawable.none_image)
//        .into(imageView)
//}