package com.example.cybersecurityeducation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class QuizList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_list)
    }

    fun toSurveyTemplate(view: View){
        val intent = Intent(this,SurveyTemplate::class.java)
        startActivity(intent)
    }
}