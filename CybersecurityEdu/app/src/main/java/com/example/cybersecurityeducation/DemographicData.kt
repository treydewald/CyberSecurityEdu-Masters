package com.example.cybersecurityeducation

import com.quickbirdstudios.surveykit.AnswerFormat

data class DemographicData(
    var q1 : QuestionData? = null,
    var q2 : QuestionData? = null,
    var q3 : QuestionData? = null,
    var q4 : QuestionData? = null,
    var q5 : QuestionData? = null,
    var q6 : QuestionData? = null
) { }

data class QuestionData(
    var question: String? = null,
    var answer: String? = null
)