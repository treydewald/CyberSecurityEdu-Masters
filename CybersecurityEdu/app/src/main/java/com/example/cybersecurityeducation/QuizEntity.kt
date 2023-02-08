package com.example.cybersecurityeducation

import com.quickbirdstudios.surveykit.steps.Step

data class QuizEntity(
    var questionStep: Step,
    var correctAnswer: String){ }