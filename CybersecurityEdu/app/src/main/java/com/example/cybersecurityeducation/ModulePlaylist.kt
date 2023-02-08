package com.example.cybersecurityeducation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Button
import android.widget.ImageButton

class ModulePlaylist : AppCompatActivity() {
    private var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_playlist)

        progressBar = findViewById<ProgressBar>(R.id.youtube_progress_bar) as ProgressBar
        val btn = findViewById(R.id.youtube_link) as ImageButton

    }
    fun toYouTubePlaylistTemplate(view: View){
        val intent = Intent(this,YoutubePlaylistTemplate::class.java)
        startActivity(intent)
    }

    fun toPreviousModule(view: View){
        val intent = Intent(this,ModuleActivity::class.java)
        startActivity(intent)
    }

    fun toNextModule(view: View){
        val intent = Intent(this,ModuleActivity::class.java)
        startActivity(intent)
    }

    fun toMainActivity(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun toModuleList(view: View){
        val intent = Intent(this,ModuleList::class.java)
        startActivity(intent)
    }
}