package com.example.cybersecurityeducation

class SurveyData(
    var q1 : singleQuestion? = null,
    var q2 : singleQuestion? = null,
    var q3 : multipleQuestion? = null,
    var q4 : singleQuestion? = null,
    var q5 : singleQuestion? = null,
    var q6 : singleQuestion? = null,
    var q7 : singleQuestion? = null,
    var q8 : singleQuestion? = null,
    var q9 : singleQuestion? = null,
    var q10 : singleQuestion? = null,
    var q11 : singleQuestion? = null,
    var q12 : singleQuestion? = null,
    var q13 : singleQuestion? = null,
    var q14 : singleQuestion? = null,
    var q15 : singleQuestion? = null,
    var q16 : singleQuestion? = null,
    var q17 : singleQuestion? = null,
    var q18 : singleQuestion? = null,
    var q19 : singleQuestion? = null,
    var q20 : singleQuestion? = null
){}

data class singleQuestion(
    var question : String? = null,
    var answer: String? = null
){}

data class multipleQuestion(
    var question : String? = null,
    var answer: ArrayList<String?>? = null
){}

