package com.example.cybersecurityeducation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget_password.*

lateinit var emailForgot: String

class ForgetPasswordActivity : AppCompatActivity() {

    private val TAG = "ForgotPassword"
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        supportActionBar?.hide() // hide action bar

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    fun toRegisterUserActivityFromForget(view: View){
        val intent = Intent(this,LoginUser::class.java)
        startActivity(intent)
        finish()
    }

    fun resetPassword(view: View){

        emailForgot   = et_forget.text.toString()
        et_forget.hideKeyboard()
        et_forget.text.clear()

        // input validation
        if(emailForgot.isEmpty()){
            et_forget.error = "Email is required!"
            et_forget.text.clear()
            et_forget.requestFocus()
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailForgot).matches()){
            et_forget.error = "Please provide a valid email!"
            et_forget.text.clear()
            et_forget.requestFocus()
        }


            if(emailForgot.isEmpty() || emailForgot.isEmpty()){ // user presses login button
                // do nothing here onCLick()
            }else{
                pb_forgot.visibility = View.VISIBLE

                    mAuth.sendPasswordResetEmail(emailForgot).addOnCompleteListener { task ->

                        if(task.isSuccessful) {
                            Log.d(TAG, "Password Reset success")
                            showDialog("Forgot Password", "Check your email to reset password")
                        }else {
                            Log.d(TAG, "Password Reset failure")
                            Log.e(TAG, "Password Reset failure: ${task.exception?.message.toString()} ")
                            showErrorDialog("Password Reset Fail",task.exception?.message.toString())
                            //showDialog("Forgot Password", "Try Again, Reset Failure")
                            pb_forgot.visibility = View.INVISIBLE
                        }
                    }
                }


            }

// utility--------------------------------------------------------------------------------------------
    private fun showDialog(title: String, descr:String) {
        // Create an alertdialog builder object,
        // then set attributes that you want the dialog to have
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(descr)

        builder.setPositiveButton("OK"){ dialog, which ->
            // code to run when YES is pressed
        }

        // create the dialog and show it
        val dialog = builder.create()
        dialog.show()
    }

    // hide keyboard
    private fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
    private fun showErrorDialog(title: String, message: String) {
        // Create an alertdialog builder object,
        // then set attributes that you want the dialog to have
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(R.drawable.ic_error_red_outline_24)
        builder.setPositiveButton("OK") { dialog, which ->
            // code to run when YES is pressed
        }
        // create the dialog and show it
        val dialog = builder.create()
        dialog.show()

    }

}