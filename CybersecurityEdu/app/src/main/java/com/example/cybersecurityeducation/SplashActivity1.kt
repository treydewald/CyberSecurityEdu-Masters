package com.example.cybersecurityeducation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash1.*

class SplashActivity1 : AppCompatActivity() {

    lateinit var splashRunnable: Runnable
    private val TAG = "Splash1"
    private lateinit var topAnimLeft : Animation
    private lateinit var bottomAnimRight : Animation
    private lateinit var middleAnim : Animation

    private var skip = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash1)
        supportActionBar?.hide()

        topAnimLeft = AnimationUtils.loadAnimation(this, R.anim.top_left_anim)
        bottomAnimRight = AnimationUtils.loadAnimation(this, R.anim.top_right_amin)
        middleAnim = AnimationUtils.loadAnimation(this, R.anim.middle_anim)

        splash_title_11.startAnimation(topAnimLeft)
        splash_title_12.startAnimation(bottomAnimRight)
        iv_logo.startAnimation(middleAnim)


        splashRunnable = Runnable {
            if(skip) {
                Log.d(TAG, "inside runnable skip1: $skip")
                val intent = Intent(this@SplashActivity1,SplashActivity2::class.java)
                startActivity(intent)
                finish()
            }
        }
        Handler(Looper.getMainLooper()).postDelayed( splashRunnable, 12000)
    }

    fun nextSplash1(view: View){
        //Handler(Looper.getMainLooper()).removeCallbacks(splashRunnable)
        skip = false
        Log.d(TAG, "inside skip1 button: $skip")
        Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)
        val intent = Intent(this@SplashActivity1,SplashActivity2::class.java)
        startActivity(intent)
        finish()
    }


}