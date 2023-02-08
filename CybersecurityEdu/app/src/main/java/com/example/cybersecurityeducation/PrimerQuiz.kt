package com.example.cybersecurityeducation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private val TAG = "Primer Quiz Activity"

private lateinit var mAuth: FirebaseAuth
private lateinit var firebaseDB: FirebaseDatabase
private lateinit var userRef: DatabaseReference

class PrimerQuiz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primer_quiz)
        supportActionBar?.hide() // hide action bar

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        // Check if user is signed in (non-null).
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser==null){
            startRegisterUser()
        }
    }

    fun nextScreenPrimer(view: View){
        val intent = Intent(this@PrimerQuiz,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startRegisterUser(){
        val intentRegister = Intent(this,LoginUser::class.java)
        startActivity(intentRegister)
        finish()
        // security related, we want the user to not have access to main activity
        // without login/sign up so main activity does not go to the call bak stack
    }
    //utility------------------------------------------------------------------------------------------
    // hide keyboard
    private fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }


}