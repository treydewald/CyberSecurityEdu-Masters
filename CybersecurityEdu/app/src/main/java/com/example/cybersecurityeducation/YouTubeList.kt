package com.example.cybersecurityeducation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ArrayAdapter



class YouTubeList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_you_tube_list)
    }
    fun toYouTubeTemplate(view: View){
        val intent = Intent(this,YoutubeTemplate::class.java)
        startActivity(intent)
    }
}