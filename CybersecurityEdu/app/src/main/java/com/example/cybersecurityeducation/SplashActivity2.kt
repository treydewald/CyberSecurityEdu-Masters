package com.example.cybersecurityeducation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash2.*

class SplashActivity2 : AppCompatActivity() {

    lateinit var splashRunnable2: Runnable
    private val TAG = "Splash2"
    private lateinit var topAnimLeft : Animation
    private lateinit var bottomAnimRight : Animation
    private lateinit var fadeAnim : Animation

    private var skip2 = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash2)
        supportActionBar?.hide()

        topAnimLeft = AnimationUtils.loadAnimation(this, R.anim.top_left_anim)
        bottomAnimRight = AnimationUtils.loadAnimation(this, R.anim.top_right_amin)
        fadeAnim = AnimationUtils.loadAnimation(this, R.anim.top_right_amin)

        tv_splash_2_title.startAnimation(topAnimLeft)
        tv_splash_2_descr.startAnimation(bottomAnimRight)
        iv_splash2.startAnimation(fadeAnim)

        splashRunnable2 = Runnable {
            if(skip2) {
                Log.d(TAG, "inside runnable skip2: $skip2")
                val intent = Intent(this@SplashActivity2,SplashActivity3::class.java)
                startActivity(intent)
                finish()
            }
        }
        Handler(Looper.getMainLooper()).postDelayed( splashRunnable2, 6000)

    }

    fun nextSplash2(view: View){
        //Handler(Looper.getMainLooper()).removeCallbacks(splashRunnable)
        skip2 = false
        Log.d(TAG, "inside skip2 button: $skip2")
        Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)
        val intent = Intent(this@SplashActivity2,SplashActivity3::class.java)
        startActivity(intent)
        finish()
    }

}