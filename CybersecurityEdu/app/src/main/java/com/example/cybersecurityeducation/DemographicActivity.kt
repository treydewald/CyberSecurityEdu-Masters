package com.example.cybersecurityeducation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_demographic.*


private val TAG = "Demographic Activity"

private lateinit var mAuth: FirebaseAuth
private lateinit var firebaseDB: FirebaseDatabase
private lateinit var userRef: DatabaseReference
private lateinit var userRef2: DatabaseReference
private lateinit var userData: UserSignUpData
var userTypeNumber: Int = 0
lateinit var answerString: String

class DemographicActivity : AppCompatActivity() {

    private var answer1: String? = ""
    private var answer2: String? = ""
    private var answer3: String? = ""
    private var answer4: String? = ""
    private var answer5: String? = ""
    private var answer6: String? = ""

    private var question1: String = ""
    private var question2: String = ""
    private var question3: String = ""
    private var question4: String = ""
    private var question5: String = ""
    private var question6: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demographic)
        supportActionBar?.hide() // hide action bar

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null).
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            startRegisterUser()
        }

        // set up realtime database
        firebaseDB = FirebaseDatabase.getInstance()
        userRef = firebaseDB.getReference("Users")

        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        userRef.child(userID).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userData: UserSignUpData? = dataSnapshot.getValue(UserSignUpData::class.java)
                Log.d(TAG, "username is: ${userData?.firstNameData}")
                demo_title.text = "Welcome ${userData?.firstNameData}"
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
            // set title to user name
        })

        rg_Q1.setOnCheckedChangeListener { _, _ ->
            answer1 =
                DemoActivity.findViewById<RadioButton>(rg_Q1.checkedRadioButtonId)?.text.toString()}
        rg_Q2.setOnCheckedChangeListener { _, _ ->
            answer2 =
                DemoActivity.findViewById<RadioButton>(rg_Q2.checkedRadioButtonId)?.text.toString()}
        rg_Q3.setOnCheckedChangeListener { _, _ ->
            answer3 =
                DemoActivity.findViewById<RadioButton>(rg_Q3.checkedRadioButtonId)?.text.toString()}
        rg_Q4.setOnCheckedChangeListener { _, _ ->
            answer4 =
                DemoActivity.findViewById<RadioButton>(rg_Q4.checkedRadioButtonId)?.text.toString()}
        rg_Q5.setOnCheckedChangeListener { _, _ ->
            answer5 =
                DemoActivity.findViewById<RadioButton>(rg_Q5.checkedRadioButtonId)?.text.toString()}
        rg_Q6.setOnCheckedChangeListener { _, _ ->
            answer6 =
                DemoActivity.findViewById<RadioButton>(rg_Q6.checkedRadioButtonId)?.text.toString()}

        // get answers
        question1 = tv_Q1.text.toString()
        question2 = tv_Q2.text.toString()
        question3 = tv_Q3.text.toString()
        question4 = tv_Q4.text.toString()
        question5 = tv_Q5.text.toString()
        question6 = tv_Q6.text.toString()
    }

    fun next(view: View) {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // set up realtime database
        firebaseDB = FirebaseDatabase.getInstance()
        userRef = firebaseDB.getReference("DemographicData")
        userRef2 = firebaseDB.getReference("Users")

        //val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val uID = mAuth.currentUser!!.uid

        //if(question1.isEmpty()){
        //    Log.d(TAG, "question 1 empty")
        //}
//        Log.d(TAG, "answer 1: $answer1")
//        Log.d(TAG, "answer 2: $answer2")
//        Log.d(TAG, "answer 3: $answer3")
//        Log.d(TAG, "answer 4: $answer4")
//        Log.d(TAG, "answer 5: $answer5")
//        Log.d(TAG, "answer 6: $answer6")

        if (answer1.isNullOrBlank() || answer2.isNullOrBlank() || answer3.isNullOrBlank() ||
            answer4.isNullOrBlank() || answer5.isNullOrBlank() || answer6.isNullOrBlank()
        ) {
            //Toast.makeText(this, "All Questions require an Answer", Toast.LENGTH_SHORT).show()
            showErrorDialog("Error", "All Questions require an Answer")
        } else {
            //Toast.makeText(this, "Execute code", Toast.LENGTH_SHORT).show()
            //Log.d(TAG, "\nquestion 1: $answer1 \n question 2: $answer2 \n question 3: $answer3 " +
            //        "\n question 4: $answer4 \n question 5: $answer5 \n question 6: $answer6")
            val userDemo = DemographicData(
                QuestionData(question1, answer1!!), QuestionData(question2, answer2!!),
                QuestionData(question3, answer3!!), QuestionData(question4, answer4!!),
                QuestionData(question5, answer5!!), QuestionData(question6, answer6!!)
            )

            setUserType(answer3!!, answer5!!)
            System.out.println("userTypeNumber: "+userTypeNumber)
            setUserTypeDatabase()

            userRef.child(uID).setValue(userDemo).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Demo data added to DB user: $uID")
                } else {
                    Log.e(TAG, "Demo data write is not successful:${task.exception} ")
                    Toast.makeText(
                        this, "Demo data write failed.",
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }

            //val intent = Intent(this, MainActivity::class.java)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun startRegisterUser() {

        val intentRegister = Intent(this, LoginUser::class.java)
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
    fun setUserType(Q3:String,Q5:String){
        if (Q3 == "12-23"){
            Log.d(TAG, Q3)
            if (Q5 == "Novice" || Q5 == "Beginner"){
                userTypeNumber = 1
                System.out.print(userTypeNumber)
            } else if (Q5 == "Competent"){
                userTypeNumber = 2
                System.out.print(userTypeNumber)
            } else if (Q5 == "Proficient" || Q5 == "Expert"){
                userTypeNumber = 3
                System.out.print(userTypeNumber)
            }
        } else if (Q3 == "24-38" || Q3 == "39-50" || Q3 == "51-65"){
            Log.d(TAG, Q3)
            if (Q5 == "Novice" || Q5 == "Beginner"){
                userTypeNumber = 4
                System.out.print(userTypeNumber)
            } else if (Q5 == "Competent"){
                userTypeNumber = 5
                System.out.print(userTypeNumber)
            } else if (Q5 == "Proficient" || Q5 == "Expert"){
                userTypeNumber = 6
                System.out.print(userTypeNumber)
            }
        } else if (Q3 == "66+"){
            Log.d(TAG, Q3)
            if (Q5 == "Novice" || Q5 == "Beginner"){
                userTypeNumber = 7
                System.out.print(userTypeNumber)
            } else if (Q5 == "Competent"){
                userTypeNumber = 8
                System.out.print(userTypeNumber)
            } else if (Q5 == "Proficient" || Q5 == "Expert"){
                userTypeNumber = 9
                System.out.print(userTypeNumber)
            }

        }
    }
    private fun setUserTypeDatabase() {
        userRef2 = firebaseDB.getReference("Users")
        val uID = mAuth.currentUser!!.uid

        userRef2.child(uID).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userData = dataSnapshot.getValue(UserSignUpData::class.java)!!
                val profileData = UserSignUpData(userData.firstNameData, userData.lastNameData, userData.emailData, userTypeNumber)
                userRef2.child(uID).setValue(profileData).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User Type Number updated for uID: $uID")
                    } else {
                        Log.e(TAG, "User Type Number failure: :${task.exception} ")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
            // set title to user name
        })

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
