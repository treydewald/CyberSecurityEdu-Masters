package com.example.cybersecurityeducation

data class DatabaseQuizData(
    var quizTitle   : String? = null,
    var quizText    : String? = null,
    var quizButtonText       : String? = null,
    var numberOfQuestions : Int? = null,
    var finishQuestionTitle : String? = null,
    var finishQuestionText : String? = null,
    var finishQuestionSubmit : String? = null,
    var quizQuestion1 : QuizQuestionData? = null,
    var quizQuestion2 : QuizQuestionData? = null,
    var quizQuestion3 : QuizQuestionData? = null,
    var quizQuestion4 : QuizQuestionData? = null,
    var quizQuestion5 : QuizQuestionData? = null,
    var quizQuestion6 : QuizQuestionData? = null,
    var quizQuestion7 : QuizQuestionData? = null,
    var quizQuestion8 : QuizQuestionData? = null,
    var quizQuestion9 : QuizQuestionData? = null,
    var quizQuestion10 : QuizQuestionData? = null

)
data class QuizQuestionData(
    var singleOrMultipleChoice: String? = null,
    var questionTitle: String? = null,
    var questionText: String? = null,
    var answer1: String? = null,
    var answer2: String? = null,
    var answer3: String? = null,
    var answer4: String? = null,
    var answer5: String? = null,
    var answer6: String? = null,
    var correctAnswer: String? = null
)