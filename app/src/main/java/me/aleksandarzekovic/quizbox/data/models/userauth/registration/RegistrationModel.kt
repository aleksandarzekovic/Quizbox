package me.aleksandarzekovic.quizbox.data.models.userauth.registration

data class RegistrationModel(
    var email: String? = null,
    var password: String? = null,
    var confirm_password: String? = null
)