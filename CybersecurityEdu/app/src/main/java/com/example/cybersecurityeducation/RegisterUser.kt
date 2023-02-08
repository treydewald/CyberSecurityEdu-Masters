package com.example.cybersecurityeducation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register_user.*


private lateinit var mAuth: FirebaseAuth
private lateinit var firebaseDB: FirebaseDatabase
private lateinit var userRef: DatabaseReference

private val TAG = "RegisterUser"

lateinit var firstName: String
lateinit var lastName: String
lateinit var email: String
lateinit var password: String

class RegisterUser : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        supportActionBar?.hide() // hide action bar

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // set up realtime database
        firebaseDB = FirebaseDatabase.getInstance()
        userRef = firebaseDB.getReference("Users")

    }

    fun registerUser(view: View) {

        firstName = et_register_first_name.text.toString()
        et_register_first_name.hideKeyboard()
        et_register_first_name.text.clear()

        lastName = et_register_last_name.text.toString()
        et_register_last_name.hideKeyboard()
        et_register_last_name.text.clear()

        email = et_register_email.text.toString()
        et_register_email.hideKeyboard()
        et_register_email.text.clear()

        password = et_register_password.text.toString()
        et_register_password.hideKeyboard()
        et_register_password.text.clear()

        // input validation
        if (firstName.isEmpty()) {
            et_register_first_name.error = "First name is required!"
            et_register_first_name.text.clear()
            et_register_first_name.requestFocus()
        }

        if (lastName.isEmpty()) {
            et_register_last_name.error = "Last name is required!"
            et_register_last_name.text.clear()
            et_register_last_name.requestFocus()
        }

        if (email.isEmpty()) {
            et_register_email.error = "Email is required!"
            et_register_email.text.clear()
            et_register_email.requestFocus()
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_register_email.error = "Please provide a valid email!"
            et_register_email.text.clear()
            et_register_email.requestFocus()
        }

        if (password.isEmpty()) {
            et_register_password.error = "Password is required!"
            et_register_password.text.clear()
            et_register_password.requestFocus()
        }
        // firebase does not accept a password of length less than 6 characters
        val minPasswordLength = 6
        if (password.length < minPasswordLength) {
            et_register_password.error = "Minimum password length should be 6 characters"
            et_register_password.text.clear()
            et_register_password.requestFocus()
        }




        if ((firstName.isEmpty() || firstName.isBlank()) || (email.isEmpty() || email.isBlank())
            || (password.isEmpty() || password.isBlank()) || (lastName.isEmpty() || lastName.isBlank())
        ) {
            // do nothing
        } else {

            pb_sign_up_circle.visibility = View.VISIBLE
            // sign up segment
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // get new user
                    val user = FirebaseAuth.getInstance().currentUser

                    Log.d(TAG, "Registered User With Email success: ${user?.displayName.toString()}")
                    Log.d(TAG, "creation time: ${user?.metadata?.creationTimestamp}")
                    Log.d(TAG, "last sign in : ${user?.metadata?.lastSignInTimestamp}")

                    //Checking for User (New/Old) // createUserWithEmailAndPassword exception should handle this
                    if (user?.metadata?.creationTimestamp == user?.metadata?.lastSignInTimestamp) {
                        //This is a New User
                        Toast.makeText(this, "Welcome New User!", Toast.LENGTH_SHORT).show()

                        // add new user data to realtime DB using auth Uid
                        val profileData = UserSignUpData(firstName, lastName, email, 0)
                        //val uID = FirebaseAuth.getInstance().currentUser?.uid
                        val uID = mAuth.currentUser!!.uid

                        userRef.child(uID).setValue(profileData).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "User data added to DB user: $uID")
                            } else {
                                Log.e(TAG, "User data write is not successful:${task.exception} ")
                                Toast.makeText(
                                    this, "User data write failed.",
                                    Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                        // user proceeds to demographic activity
                        pb_sign_up_circle.visibility = View.INVISIBLE
                        // send verification email for nex time sign in
                        //user?.sendEmailVerification()
                        // Since the user signed in, the user can go back to main activity
                        //val intent = Intent(this, DemographicActivity::class.java)
                        val intent = Intent(this, DemographicActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    /**
                    else {
                        //This is a returning user
                        Toast.makeText(this, "An Account with Email $email already exists. \nLog in or Reset password", Toast.LENGTH_SHORT).show()
                        pb_sign_up_circle.visibility = View.INVISIBLE
                        // redirect to login page
                        val intent = Intent(this, LoginUser::class.java)
                        startActivity(intent)
                        finish()
                    }
                    */
                } else {
                    Log.e(TAG, "Sign up is not successful:${task.exception?.message.toString()} ")
                    showErrorDialog("Registration Failed",task.exception?.message.toString())
                    //Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show();
                    pb_sign_up_circle.visibility = View.INVISIBLE
                }

            }

        }

    }

    fun toLoginUserActivity(view: View) {
        val intent = Intent(this, LoginUser::class.java)
        startActivity(intent)
        //finish()
    }

    //utility---------------------------------------------------------------------------------------
// hide keyboard
    private fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun showErrorDialog(title : String, message: String) {
        // Create an alertdialog builder object,
        // then set attributes that you want the dialog to have
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(R.drawable.ic_error_red_outline_24)
        builder.setPositiveButton("OK"){ dialog, which ->
            // code to run when YES is pressed
        }

        // create the dialog and show it
        val dialog = builder.create()
        dialog.show()
    }
}