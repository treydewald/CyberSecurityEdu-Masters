package com.example.cybersecurityeducation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ResultsActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    private lateinit var firebaseDB: FirebaseDatabase
    private lateinit var demographicRef: DatabaseReference
    private lateinit var userRef: DatabaseReference
    private lateinit var quizRef: DatabaseReference
    var questionsArray = ArrayList<QuestionData>()
    private lateinit var questions: DemographicData
    lateinit var viewModel: UserTypeViewModel
    lateinit var userType: String
    lateinit var tipsIntent: Intent
    lateinit var articleIntent: Intent
    lateinit var videoIntent: Intent
    lateinit var quizIntent: Intent
    lateinit var quizType: String
    lateinit var levelCheck: String
    lateinit var quizLevel: String

    var userResponse1: String = ""
    var userResponse2: String = ""
    var userResponse3: String = ""
    var userResponse4: String = ""
    var userResponse5: String = ""

    //private val questionArray = ArrayList<String?>()
    //private val correctArrayBool = ArrayList<Boolean>()
    private val dataArray = ArrayList<ResultData>()

    lateinit var recyclerViewResult: RecyclerView
    lateinit var resultAdapter: ResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        quizType = intent.getStringExtra("type").toString()
        quizLevel = intent.getStringExtra("level").toString()

        mAuth = FirebaseAuth.getInstance()

        firebaseDB = FirebaseDatabase.getInstance()
        userRef = firebaseDB.getReference("Users")
        quizRef = firebaseDB.getReference("Quizzes")

        recyclerViewResult = findViewById(R.id.recyclerViewResult)
        // to go horizontal scrollable list
        recyclerViewResult.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        getQuizData(quizType, quizLevel)
    }

    fun getQuizData(quizType: String, quizLevel: String) {
        quizRef.child(quizLevel).child(quizType).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val databaseQuizData: DatabaseQuizData? =
                    dataSnapshot.getValue(DatabaseQuizData::class.java)
                System.out.println("DatabaseQuizTemplate onDataChange")
                System.out.println("quizTitle is " + databaseQuizData?.quizTitle)

                val correctAnswer1: String?  = databaseQuizData?.quizQuestion1?.correctAnswer
                userResponse1 = intent.getStringExtra("Question 1").toString()
                val correct1  = correctAnswer1 == userResponse1
                val question1 = databaseQuizData?.quizQuestion1?.questionText
                dataArray.add(ResultData(question1!!,userResponse1,correct1))

                val correctAnswer2: String?= databaseQuizData?.quizQuestion2?.correctAnswer
                userResponse2 = intent.getStringExtra("Question 2").toString()
                val correct2 = correctAnswer2 == userResponse2
                val question2 = databaseQuizData?.quizQuestion2?.questionText
                dataArray.add(ResultData(question2!!,userResponse2,correct2))

                val correctAnswer3: String? = databaseQuizData?.quizQuestion3?.correctAnswer
                userResponse3 = intent.getStringExtra("Question 3").toString()
                val correct3 = correctAnswer3 == userResponse3
                val question3 = databaseQuizData?.quizQuestion3?.questionText
                dataArray.add(ResultData(question3!!,userResponse3,correct3))

                val correctAnswer4: String? = databaseQuizData?.quizQuestion4?.correctAnswer
                userResponse4 = intent.getStringExtra("Question 4").toString()
                val correct4 = correctAnswer4 == userResponse4
                val question4 = databaseQuizData?.quizQuestion4?.questionText
                dataArray.add(ResultData(question4!!,userResponse4,correct4))

                val correctAnswer5: String? = databaseQuizData?.quizQuestion5?.correctAnswer
                userResponse5 = intent.getStringExtra("Question 5").toString()
                val correct5 = correctAnswer5 == userResponse5
                val question5 = databaseQuizData?.quizQuestion5?.questionText
                dataArray.add(ResultData(question5!!,userResponse5,correct5))



                // add recycler view stuff
                resultAdapter = ResultsAdapter(dataArray)
                recyclerViewResult.adapter = resultAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
            // set title to user name
        })
    }

    fun toMainActivityResult(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

/**

var question1Result = ""
var correctAnswer1: String? = databaseQuizData?.quizQuestion1?.correctAnswer
if (correctAnswer1 == userResponse1) {
question1Result = "correct!"
} else {
question1Result = "incorrect."
}
//val textView: TextView = findViewById(R.id.question1)
//textView.text = "Question 1: "+databaseQuizData?.quizQuestion1?.questionText+"\nYour answer was: " + answer1.toString()+"\nThe correct answer was: "+ correctAnswer1+"\nYou were "+question1Result

var correctAnswer2: String? = databaseQuizData?.quizQuestion2?.correctAnswer
answer2 = intent.getStringExtra("Question 2").toString()
var question2Result = ""
if (correctAnswer2 == answer2) {
question2Result = "correct!"
} else {
question2Result = "incorrect."
}
//val textView2: TextView = findViewById(R.id.question2)
//textView2.text = "Question 2: "+databaseQuizData?.quizQuestion2?.questionText+"\nYour answer was: " + answer2 +"\nThe correct answer was: "+ correctAnswer2+"\nYou were "+question2Result

var correctAnswer3: String? = databaseQuizData?.quizQuestion3?.correctAnswer
answer3 = intent.getStringExtra("Question 3").toString()
var question3Result = ""
if (correctAnswer3 == answer3) {
question3Result = "correct!"
} else {
question3Result = "incorrect."
}
//val textView3: TextView = findViewById(R.id.question3)
//textView3.text = "Question 3: "+databaseQuizData?.quizQuestion3?.questionText+"\nYour answer was: " + answer3.toString()+"\nThe correct answer was: "+ correctAnswer3+"\nYou were "+question3Result

var correctAnswer4: String? = databaseQuizData?.quizQuestion4?.correctAnswer
answer4 = intent.getStringExtra("Question 4").toString()
var question4Result = ""
if (correctAnswer4 == answer4) {
question4Result = "correct!"
} else {
question4Result = "incorrect."
}
//val textView4: TextView = findViewById(R.id.question4)
//textView4.text = "Question 4: "+databaseQuizData?.quizQuestion4?.questionText+"\nYour answer was: " + answer4.toString()+"\nThe correct answer was: "+ correctAnswer4+"\nYou were "+question4Result

var correctAnswer5: String? = databaseQuizData?.quizQuestion5?.correctAnswer
answer5 = intent.getStringExtra("Question 5").toString()
var question5Result = ""
if (correctAnswer5 == answer5) {
question5Result = "correct!"
} else {
question5Result = "incorrect."
}
//val textView5: TextView = findViewById(R.id.question5)
//textView5.text = "Question 5: "+databaseQuizData?.quizQuestion5?.questionText+"\nYour answer was: " + answer5.toString()+"\nThe correct answer was: "+ correctAnswer5+"\nYou were "+question5Result

 */