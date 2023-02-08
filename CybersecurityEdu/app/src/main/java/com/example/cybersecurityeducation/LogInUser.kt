package com.example.cybersecurityeducation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login_user.*

lateinit var emailLogIn: String
lateinit var passwordLogIn: String
private lateinit var firebaseDB: FirebaseDatabase
private lateinit var userRef: DatabaseReference

class LoginUser : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 220
    }

    private val TAG = "LogInUser"
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)
        supportActionBar?.hide()

        // set up realtime database
        firebaseDB = FirebaseDatabase.getInstance()
        userRef = firebaseDB.getReference("Users")

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // [START config_signin]
        // Configure Google Sign In Options
        // R.String.default_web_client_id is the id from the google-services json
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    // start activity to sign up user ------------------------------------------------------------
    fun toRegisterUserActivity(view: View) {
        val intent = Intent(this, RegisterUser::class.java)
        startActivity(intent)
        finish()
    }

    // start activity to reset password ----------------------------------------------------------
    fun forgotPassword(view: View) {
        val intent = Intent(this, ForgetPasswordActivity::class.java)
        startActivity(intent)
        finish()
    }

    //----------------------- Google sign in/up methods-------------------------------------------
    fun googleSignIn(view: View) {
        signInGoogle()
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e)
                    // ...
                }
            } else {
                Log.e(TAG, "Sign in is not successful:${task.exception} ")
                //Toast.makeText(this, "Sign in failed.", Toast.LENGTH_SHORT).show();
                showErrorDialog("Sign in Failed", task.exception?.message.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = mAuth.currentUser

                    Log.d(TAG, "creation time: ${user?.metadata?.creationTimestamp}")
                    Log.d(TAG, "last sign in: ${user?.metadata?.lastSignInTimestamp}")

                    //Checking for User (New/Old)
                    if (user?.metadata?.creationTimestamp == user?.metadata?.lastSignInTimestamp) {

                        //This is a New User
                        Toast.makeText(
                            this,
                            "Welcome New user: ${user?.displayName}",
                            Toast.LENGTH_SHORT
                        ).show()

                        val fullName = user?.displayName
                        var parts = fullName?.split(" ")?.toMutableList()
                        val firstNameGoogle = parts?.get(0)?.toString()
                        parts?.removeAt(0)
                        val lastNameGoogle = parts?.joinToString(" ")
                        val emailGoogle = user?.email

                        Log.d(
                            TAG,
                            "firstName: $firstNameGoogle lastName: $lastNameGoogle email: $emailGoogle"
                        )

                        // add new user data to realtime DB using auth Uid
                        val profileData =
                            UserSignUpData(firstNameGoogle, lastNameGoogle, emailGoogle)
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

                        // send verification email for nex time sign in
                        user?.sendEmailVerification()
                        // Since the user signed in, the user can go back to main activity
                        val intent = Intent(this, DemographicActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // returning user
                        //This is a returning user
                        Log.w(TAG, "Returning User ${user?.displayName}")
                        Toast.makeText(
                            this,
                            "Welcome Back user ${user?.displayName} ",
                            Toast.LENGTH_SHORT
                        ).show()
                        // redirect to main activity
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    showErrorDialog("Sign in Credential Failed", task.exception?.message.toString())
                }
            }
    }

    //-------------------------- Sign in with EMAIL & PASSWORD -------------------------------------
    fun login(view: View) {

        emailLogIn = et_login_email.text.toString()
        et_login_email.hideKeyboard()
        et_login_email.text.clear()

        passwordLogIn = et_login_password.text.toString()
        et_login_password.hideKeyboard()
        et_login_password.text.clear()

        // input validation


        if (emailLogIn.isEmpty()) {
            et_login_email.error = "Email is required!"
            et_login_email.text.clear()
            et_login_email.requestFocus()
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailLogIn).matches()) {
            et_login_email.error = "Please provide a valid email!"
            et_login_email.text.clear()
            et_login_email.requestFocus()
        }

        if (passwordLogIn.isEmpty()) {
            et_login_password.error = "Password is required!"
            et_login_password.text.clear()
            et_login_password.requestFocus()
        }
        // firebase does not accept a password of length less than 6 characters
        val minPasswordLength = 6
        if (passwordLogIn.length < minPasswordLength) {
            et_login_password.error = "Minimum password length should be 6 characters"
            et_login_password.text.clear()
            et_login_password.requestFocus()
        }

        Log.d(TAG, "EMAIL: $emailLogIn  PASSWORD: $passwordLogIn")

        if (emailLogIn.isEmpty() || emailLogIn.isEmpty()) { // user presses login button
            // do nothing here onCLick()
        } else if (view == btn_logIn) {
            pb_log_in_circle.visibility = View.VISIBLE
            mAuth.signInWithEmailAndPassword(emailLogIn, passwordLogIn)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // get new user
                        val user = FirebaseAuth.getInstance().currentUser

                        if (user != null) {
                            if (user.isEmailVerified) {
                                pb_log_in_circle.visibility = View.INVISIBLE
                                Log.d(TAG, "Sign In User With Email success: ${user.uid}")
                                //This is a returning user // not sure about this part
                                Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show()
                                // redirect to main activity
                                // Since the user signed in, the user can go back to main activity
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                // Make sure to call finish(), otherwise the user would be able to go back to the RegisterActivity, not good
                                finish()
                            } else {
                                user.sendEmailVerification().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "Email sent.")
                                    }
                                }
                                Toast.makeText(
                                    this,
                                    "Check your Email to verify your account and sign in again"
                                    ,
                                    Toast.LENGTH_SHORT
                                ).show()
                                pb_log_in_circle.visibility = View.INVISIBLE
                            }
                        }
                    } else {
                        Log.e(TAG, "Sign in is not successful:${task.exception} ")
                        showErrorDialog("Sign In Failed", task.exception?.message.toString())
                        //Toast.makeText(this, "Sign in failed.", Toast.LENGTH_SHORT).show();
                        pb_log_in_circle.visibility = View.INVISIBLE
                    }
                }

        } //else if  {
        // do something
        //}

    }

    //utility--------------------------------------------------------------------------------------
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