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
import kotlinx.android.synthetic.main.activity_splash3.*

class SplashActivity3 : AppCompatActivity() {

    lateinit var splashRunnable3: Runnable
    private val TAG = "Splash3"
    private lateinit var topAnim : Animation
    private lateinit var bottomAnim : Animation
    private lateinit var middleAnim : Animation

    private var skip3 = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash3)
        supportActionBar?.hide()

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_left_anim)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.top_right_amin)
        middleAnim = AnimationUtils.loadAnimation(this, R.anim.middle_anim)

        tv_splash_3_title.startAnimation(middleAnim)
        tv_splash_3_descr.startAnimation(bottomAnim)
        iv_splash3.startAnimation(topAnim)

        splashRunnable3 = Runnable {
            if(skip3) {
                Log.d(TAG, "inside runnable skip3: $skip3")
                val intent = Intent(this@SplashActivity3,LoginUser::class.java)
                startActivity(intent)
                finish()
            }
        }
        Handler(Looper.getMainLooper()).postDelayed( splashRunnable3, 6000)

    }

    fun nextSplash3(view: View){
        //Handler(Looper.getMainLooper()).removeCallbacks(splashRunnable)
        skip3 = false
        Log.d(TAG, "inside skip3 button: $skip3")
        Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)
        val intent = Intent(this@SplashActivity3,LoginUser::class.java)
        startActivity(intent)
        finish()
    }

}