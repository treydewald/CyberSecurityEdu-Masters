package com.example.cybersecurityeducation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.quickbirdstudios.surveykit.*
import com.quickbirdstudios.surveykit.result.TaskResult
import com.quickbirdstudios.surveykit.steps.CompletionStep
import com.quickbirdstudios.surveykit.steps.InstructionStep
import com.quickbirdstudios.surveykit.steps.QuestionStep
import com.quickbirdstudios.surveykit.steps.Step
import com.quickbirdstudios.surveykit.survey.SurveyView
import kotlin.system.exitProcess


private lateinit var firebaseDB: FirebaseDatabase
private lateinit var userRef: DatabaseReference
private lateinit var quizRef: DatabaseReference
private lateinit var quizData: QuizData

class DatabaseQuizTemplate : AppCompatActivity() {

    private val TAG = "QuizActivityDataBase"

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var quizType : String
    lateinit var levelCheck : String
    lateinit var quizLevel : String
    private lateinit var survey: SurveyView
    private lateinit var container: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_quiz_template)
        survey = findViewById(R.id.survey_view)
        container = findViewById(R.id.surveyContainer)

        quizType = intent.getStringExtra("type").toString()
        levelCheck = intent.getStringExtra("level").toString()
        if (levelCheck == "EASY"){
            quizLevel = "Level1"
        } else if (levelCheck == "MEDIUM"){
            quizLevel = "Level2"
        } else if (levelCheck == "HARD"){
            quizLevel = "Level3"
        } else {
            // Default to Level 1
            quizLevel = "Level1"
        }
        System.out.println(quizLevel)
        System.out.println(quizType+" "+ quizLevel)

        mAuth = FirebaseAuth.getInstance()

        firebaseDB = FirebaseDatabase.getInstance()
        userRef = firebaseDB.getReference("Users")
        quizRef = firebaseDB.getReference("Quizzes")

        getQuizData(quizType, quizLevel)
    }
    fun getQuizData(quizType: String, quizLevel: String){
        quizRef.child(quizLevel).child(quizType).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val databaseQuizData: DatabaseQuizData? = dataSnapshot.getValue(DatabaseQuizData::class.java)
                System.out.println("DatabaseQuizTemplate onDataChange")
                System.out.println("quizTitle is "+ databaseQuizData?.quizTitle.toString())
                setupSurvey(survey, databaseQuizData)
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
            // set title to user name
        })
    }
    private fun setupSurvey(surveyView: SurveyView, databaseQuizData: DatabaseQuizData?) {

        val numberOfQuestions = databaseQuizData?.numberOfQuestions
        var quizQuestionPositions : List<QuizQuestionData?> = listOf(databaseQuizData?.quizQuestion1, databaseQuizData?.quizQuestion2,
            databaseQuizData?.quizQuestion3, databaseQuizData?.quizQuestion4, databaseQuizData?.quizQuestion5)
        System.out.println(quizQuestionPositions)
        var emptyQuestionStep = QuestionStep(
            title = "",
            text = "",
            answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                textChoices = listOf(
                    TextChoice("")
                )
            )
        )
        var questionStepList : MutableList<Step> = mutableListOf(emptyQuestionStep, emptyQuestionStep, emptyQuestionStep, emptyQuestionStep, emptyQuestionStep)
        for (i in 0..numberOfQuestions!!-1) {
            questionStepList[i] = QuestionStep(
                title = quizQuestionPositions[i]?.questionTitle.toString(),
                text = quizQuestionPositions[i]?.questionText.toString(),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(quizQuestionPositions[i]?.answer1.toString()),
                        TextChoice(quizQuestionPositions[i]?.answer2.toString()),
                        TextChoice(quizQuestionPositions[i]?.answer3.toString()),
                        TextChoice(quizQuestionPositions[i]?.answer4.toString())
                    )
                )
            )
            System.out.println(quizQuestionPositions[i]?.questionTitle)
        }
        System.out.println("Finish question title: "+databaseQuizData?.finishQuestionTitle.toString())

        val steps = listOf(
            InstructionStep(
                title = databaseQuizData?.quizTitle.toString(),
                text = databaseQuizData?.quizText.toString(),
                buttonText = databaseQuizData?.quizButtonText.toString()
            ),
            if (questionStepList[0] != emptyQuestionStep) {
                questionStepList[0]
            } else {
                System.out.println("Question empty")
            },
            if (questionStepList[1] != emptyQuestionStep) {
                questionStepList[1]
            } else {
                System.out.println("Question empty")
            },
            if (questionStepList[2] != emptyQuestionStep) {
                questionStepList[2]
            } else {
                System.out.println("Question empty")
            },
            if (questionStepList[3] != emptyQuestionStep) {
                questionStepList[3]
            } else {
                System.out.println("Question empty")
            },
            if (questionStepList[4] != emptyQuestionStep) {
                questionStepList[4]
            } else{
                System.out.println("Question empty")
            },
            CompletionStep(
                title = databaseQuizData?.finishQuestionTitle.toString(),
                text = databaseQuizData?.finishQuestionText.toString(),
                buttonText = databaseQuizData?.finishQuestionSubmit.toString()
            )
        )


        val task = NavigableOrderedTask(steps = steps as List<Step>)

        task.setNavigationRule(
            steps[0].id,
            NavigationRule.DirectStepNavigationRule(
                destinationStepStepIdentifier = steps[1].id
            )
        )
        var resultsArray = mutableListOf<String>()
        surveyView.onSurveyFinish = { taskResult: TaskResult, reason: FinishReason ->
            if (reason == FinishReason.Completed) {
                taskResult.results.forEach { stepResult ->
                    Log.d("ASDF", "answer ${stepResult.results.firstOrNull()}")
                    var answer = stepResult.results.firstOrNull().toString().substringAfter("value=").dropLast(2)
                    Log.d("BBBB", "${answer}")
                    if (answer.contains("IntroQuestionResult") || answer.contains("FinishQuestionResult")) {
                    } else {
                        resultsArray.add(answer)
                    }
                    container.removeAllViews()
                }
                val intent = Intent(this,ResultsActivity::class.java)
                System.out.println(resultsArray)
                for (i in 1..resultsArray.size){
                    System.out.println("For loop at "+i)
                    intent.putExtra("Question "+i,resultsArray[i-1])
                }
                intent.putExtra("type", quizType)
                intent.putExtra("level", quizLevel)
                startActivity(intent)
            }
        }

        val configuration = SurveyTheme(
            themeColorDark = ContextCompat.getColor(this, R.color.cyan_normal),
            themeColor = ContextCompat.getColor(this, R.color.cyan_normal),
            textColor = ContextCompat.getColor(this, R.color.cyan_text)
        )

        surveyView.start(task, configuration)
    }
    fun toMainActivity(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}