package com.example.cybersecurityeducation



data class QuizData(
    var question1 : SingleResponse,
    var question2 : SingleResponse,
    var question3 : SingleResponse,
    var question4 : SingleResponse,
    var question5 : SingleResponse
) {
}


data class SingleResponse(
    var questionTitle : String,
    var correctAnswer: String,
    var userResponse: String,
    var corectOrNot: Boolean
){}