package com.example.cybersecurityeducation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View


class ModuleList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_list)
    }
    fun toModuleActivity(view: View){
        val intent = Intent(this,ModuleActivity::class.java)
        startActivity(intent)
    }
    fun toModulePlaylist(view: View){
        val intent = Intent(this,ModulePlaylist::class.java)
        startActivity(intent)
    }
    fun toQuizList(view: View){
        val intent = Intent(this,QuizList::class.java)
        startActivity(intent)
    }
}