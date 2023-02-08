package com.example.cybersecurityeducation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    private lateinit var firebaseDB: FirebaseDatabase
    private lateinit var demographicRef: DatabaseReference
    private lateinit var userRef: DatabaseReference
    private lateinit var surveyRef: DatabaseReference
    var questionsArray = ArrayList<QuestionData>()
    private lateinit var questions: DemographicData
    lateinit var viewModel: UserTypeViewModel
    lateinit var userType : String
    lateinit var tipsIntent: Intent
    lateinit var articleIntent: Intent
    lateinit var videoIntent: Intent
    lateinit var quizIntent: Intent
    lateinit var tensorIntent: Intent
    lateinit var answers: String

    lateinit var progressbar2FA : ProgressBar
    lateinit var progressbar2FAText : TextView

    lateinit var progressbarPass : ProgressBar
    lateinit var progressbarPassText : TextView

    lateinit var progressbarSoft : ProgressBar
    lateinit var progressbarSoftText : TextView

    lateinit var progressbarPhish : ProgressBar
    lateinit var progressbarPhishText : TextView


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide() // hide action bar
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        var answerArray = arrayOf<String>()
        var userTypeNumber = 0

        // set progress bars

        var twoFAProgress = 10
        var phishProgress = 5
        var softProgress = 15
        var passProgress = 8
        progressbar2FA = findViewById(R.id.progressBar2FA)
        progressbar2FAText = findViewById(R.id.progressBar2FA_tv)

        progressbarPass = findViewById(R.id.progressBarPass)
        progressbarPassText = findViewById(R.id.progressBarPass_tv)

        progressbarPhish = findViewById(R.id.progressBarPhish)
        progressbarPhishText = findViewById(R.id.progressBarPhish_tv)

        progressbarSoft = findViewById(R.id.progressBarSoft)
        progressbarSoftText = findViewById(R.id.progressBarSoft_tv)

        progressbar2FA.progress = twoFAProgress
        progressbar2FAText.text = "$twoFAProgress%"

        progressbarPhish.progress = phishProgress
        progressbarPhishText.text = "$phishProgress%"

        progressbarSoft.progress = softProgress
        progressbarSoftText.text = "$softProgress%"

        progressbarPass.progress = passProgress
        progressbarPassText.text = "$passProgress%"



        // Check if user is signed in (non-null).
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            startLoginUser()
        }

        // [START config_signin]
        // Configure Google Sign In Options
        // R.String.default_web_client_id is the id from the google-services json
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        firebaseDB = FirebaseDatabase.getInstance()
        demographicRef = firebaseDB.getReference("DemographicData")
        userRef = firebaseDB.getReference("Users")
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        demographicRef.child(userID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val question: DemographicData? = dataSnapshot.getValue(DemographicData::class.java)
                Log.d(TAG, "Question 3 answer is: ${question?.q3?.answer}​​​​​")
                Log.d(TAG, "Question 5 answer is: ${question?.q5?.answer}​​​​​")
                if (question != null) {
                    questions = question
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
            // set title to user name
        })

        userRef.child(userID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val userData: UserSignUpData? = dataSnapshot.getValue(UserSignUpData::class.java)
                userTypeNumber = userData?.userTypeNumber!!
                System.out.println("onDataChange MainActivity " +userTypeNumber)
                setArticleLevel()
                setTipsLevel()
                setQuizLevel()
                setVideoLevel()
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
            // set title to user name
        })

    }


    fun logout(view: View) {

        // User chose the "logout" item, logout the user then
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        mAuth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.e(TAG, "Google Sign out successful ")
                Toast.makeText(this, "Logout: Google", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "Google Sign out is not successful:${task.exception} ")
            }

        }

        if (mAuth.currentUser == null) {
            startLoginUser()
        } else {
            Log.e(TAG, "Sign out is not successful ")
        }
    }
    //----------- Start TipsActivity ---------------------------------------------------------------
    fun toTipsActivity(view: View) {
        startActivity(tipsIntent)
    }
    fun toSecuritySurvey(view: View) {
        val intent = Intent(this, SecuritySurvey::class.java)
        startActivity(intent)
    }
    fun toTensorFlow(view: View) {
        tensorIntent = Intent(this, TensorFlow::class.java)
        getSurveyAnswers()
    }
    fun getSurveyAnswers(){
        surveyRef = firebaseDB.getReference("SurveyData")
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        surveyRef.child(userID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val surveyData: SurveyData? = dataSnapshot.getValue(SurveyData::class.java)
                val answers = surveyData?.q1?.answer +" "+surveyData?.q2?.answer +" " +surveyData?.q3?.answer +" " +surveyData?.q4?.answer  +" "+surveyData?.q5?.answer +" " +surveyData?.q6?.answer +" " +surveyData?.q7?.answer +" " +surveyData?.q8?.answer +" " +surveyData?.q9?.answer +" " +surveyData?.q10?.answer +" " +surveyData?.q11?.answer +" " +surveyData?.q12?.answer +" " +surveyData?.q13?.answer +" " +surveyData?.q14?.answer +" " +surveyData?.q15?.answer +" " +surveyData?.q16?.answer +" " +surveyData?.q17?.answer +" " +surveyData?.q18?.answer +" " +surveyData?.q19?.answer +" " +surveyData?.q20?.answer
                answers.replace("(", "")
                answers.replace(")", "")
                answers.replace("[", "")
                answers.replace("]", "")
                System.out.println(answers)
                tensorIntent.putExtra("answers", answers)
                startActivity(tensorIntent)

            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                System.out.println("Failed")
            }
            // set title to user name
        })
    }

    fun toQuizActivity2FA(view: View){
        quizIntent.putExtra("type", "TwoFactorAuthentication")
        startActivity(quizIntent)
    }

    fun toQuizActivityPasswordManagers(view: View){
        quizIntent.putExtra("type", "PasswordManagers")
        startActivity(quizIntent)
    }
    fun toQuizActivityPhishing(view: View){
        quizIntent.putExtra("type", "Phishing")
        startActivity(quizIntent)
    }
    fun toQuizActivitySoftwareUpdates(view: View){
        quizIntent.putExtra("type", "SoftwareUpdates")
        startActivity(quizIntent)
    }
    fun toQuizActivityPrivacy(view: View){
        quizIntent.putExtra("type", "Privacy")
        startActivity(quizIntent)
    }

    //----------- Start ArticleActivity for 2FA----------------------------------------------------
    fun toArticleActivity2FA(view: View) {
        val topic = "2FA"
        articleIntent.putExtra("2FA", topic)
        startActivity(articleIntent)
    }

    //----------- Start ArticleActivity for PHISHING----------------------------------------------------
    fun toArticleActivityPASSWORDMANAGERS(view: View) {
        val topic = "PASSWORDMANAGERS"
        articleIntent.putExtra("PASSWORDMANAGERS", topic)
        startActivity(articleIntent)
    }
    //----------- Start ArticleActivity for PRIVACY----------------------------------------------------
    fun toArticleActivityPRIVACY(view: View) {
        val topic = "PRIVACY"
        articleIntent.putExtra("PRIVACY", topic)
        startActivity(articleIntent)
    }
    //----------- Start ArticleActivity for SOFTWARE UPDATES----------------------------------------------------
    fun toArticleActivitySOFTWAREUPDATES(view: View) {
        val topic = "SOFTWAREUPDATES"
        articleIntent.putExtra("SOFTWAREUPDATES", topic)
        startActivity(articleIntent)
    }
    //----------- Start ArticleActivity for STRONG PASSWORDS----------------------------------------------------
    fun toArticleActivitySTRONGPASSWORDS(view: View) {
        val topic = "STRONGPASSWORDS"
        articleIntent.putExtra("STRONGPASSWORDS", topic)
        startActivity(articleIntent)
    }
    //----------- Start ArticleActivity for PHISHING----------------------------------------------------
    fun toArticleActivityPHISHING(view: View) {
        val topic = "PHISHING"
        articleIntent.putExtra("PHISHING", topic)
        startActivity(articleIntent)
    }

    //----------- Start YouTube activity for 2FA----------------------------------------------------
    fun toYTActivity2FA(view: View) {
        val topic = "2FA"
        videoIntent.putExtra("2FA", topic)
        startActivity(videoIntent)
    }
    //----------- Start YouTube activity for Phishing----------------------------------------------------
    fun toYTActivityPhishing(view: View) {
        val topic = "PHISHING"
        videoIntent.putExtra("PHISHING", topic)
        startActivity(videoIntent)
    }
    //----------- Start YouTube activity for Software Updates----------------------------------------------------
    fun toYTActivitySoftwareUpdates(view: View) {
        val topic = "SOFTWAREUPDATES"
        videoIntent.putExtra("SOFTWAREUPDATES", topic)
        startActivity(videoIntent)
    }
    //----------- Start YouTube activity for Password Managers----------------------------------------------------
    fun toYTActivityPasswordManagers(view: View) {
        val topic = "PASSWORDMANAGERS"
        videoIntent.putExtra("PASSWORDMANAGERS", topic)
        startActivity(videoIntent)
    }

    fun toYouTubeTemplate(view: View) {
        val intent = Intent(this, YoutubeTemplate::class.java)
        startActivity(intent)
    }

    fun toSurveyTemplate(view: View) {
        val intent = Intent(this, SurveyTemplate::class.java)
        startActivity(intent)
    }

    fun toModuleList(view: View) {
        val intent = Intent(this, ModuleList::class.java)
        startActivity(intent)
    }

    fun toDatabaseQuizTemplate(view: View){
        val intent = Intent(this,DatabaseQuizTemplate::class.java)
        intent.putExtra("type", "PasswordManagers")
        intent.putExtra("level", "Level1")
        startActivity(intent)
    }

    fun toModuleActivity(view: View) {
        val intent = Intent(this, ModuleActivity::class.java)
        startActivity(intent)
    }

    fun toDummySurveyActivity(view: View) {
        val intent = Intent(this, QuizActivity::class.java)
        startActivity(intent)
    }

    fun setArticleLevel(){
        var difficultyLevel = ""
        if (userTypeNumber == 1 || userTypeNumber == 2 || userTypeNumber == 3){
            difficultyLevel = "EASY"
            Log.d(TAG, "Article difficulty level: "+ difficultyLevel.toString())
        } else if (userTypeNumber == 4 || userTypeNumber == 5 || userTypeNumber == 6){
            difficultyLevel = "MEDIUM"
            Log.d(TAG, "Article difficulty level: "+difficultyLevel.toString())
        } else if (userTypeNumber == 7 || userTypeNumber == 8 || userTypeNumber == 9){
            difficultyLevel = "HARD"
            Log.d(TAG, "Article difficulty level: "+difficultyLevel.toString())
        }
        articleIntent = Intent(this, ArticleActivity::class.java)
        articleIntent.putExtra("USER_TYPE_ARTICLE", difficultyLevel)
    }
    fun setTipsLevel(){
        var difficultyLevel = ""
        if (userTypeNumber == 1 || userTypeNumber == 2 || userTypeNumber == 3){
            difficultyLevel = "EASY"
            Log.d(TAG, "Tips difficulty level: "+ difficultyLevel.toString())
        } else if (userTypeNumber == 4 || userTypeNumber == 5 || userTypeNumber == 6){
            difficultyLevel = "MEDIUM"
            Log.d(TAG, "Tips difficulty level: "+difficultyLevel.toString())
        } else if (userTypeNumber == 7 || userTypeNumber == 8 || userTypeNumber == 9){
            difficultyLevel = "HARD"
            Log.d(TAG, "Tips difficulty level: "+difficultyLevel.toString())
        }
        tipsIntent = Intent(this, TipsActivity::class.java)
        tipsIntent.putExtra("USER_TYPE_TIPS", difficultyLevel)
    }
    fun setQuizLevel(){
        var difficultyLevel = ""
        if (userTypeNumber == 1 || userTypeNumber == 4 || userTypeNumber == 7){
            difficultyLevel = "EASY"
            Log.d(TAG, "Quiz difficulty level: "+difficultyLevel.toString())
        } else if (userTypeNumber == 2 || userTypeNumber == 5 || userTypeNumber == 8){
            difficultyLevel = "MEDIUM"
            Log.d(TAG, "Quiz difficulty level: "+difficultyLevel.toString())
        } else if (userTypeNumber == 3 || userTypeNumber == 6 || userTypeNumber == 9){
            difficultyLevel = "HARD"
            Log.d(TAG, "Quiz difficulty level: "+difficultyLevel.toString())
        }
        quizIntent = Intent(this, DatabaseQuizTemplate::class.java)
        quizIntent.putExtra("level", difficultyLevel)
    }
    fun setVideoLevel(){
        var difficultyLevel = ""
        if (userTypeNumber == 1 || userTypeNumber == 4 || userTypeNumber == 7){
            difficultyLevel = "EASY"
            Log.d(TAG, "Video difficulty level: "+difficultyLevel.toString())
        } else if (userTypeNumber == 2 || userTypeNumber == 5 || userTypeNumber == 8){
            difficultyLevel = "MEDIUM"
            Log.d(TAG, "Video difficulty level: "+difficultyLevel.toString())
        } else if (userTypeNumber == 3 || userTypeNumber == 6 || userTypeNumber == 9){
            difficultyLevel = "HARD"
            Log.d(TAG, "Video difficulty level: "+difficultyLevel.toString())
        }
        videoIntent = Intent(this, YoutubeActivity::class.java)
        videoIntent.putExtra("USER_TYPE_VIDEO", difficultyLevel)
    }

    private fun startLoginUser() {
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

}