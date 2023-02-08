package com.example.cybersecurityeducation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.quickbirdstudios.surveykit.*
import com.quickbirdstudios.surveykit.result.TaskResult
import com.quickbirdstudios.surveykit.steps.CompletionStep
import com.quickbirdstudios.surveykit.steps.InstructionStep
import com.quickbirdstudios.surveykit.steps.QuestionStep
import com.quickbirdstudios.surveykit.steps.Step
import com.quickbirdstudios.surveykit.survey.SurveyView

class QuizActivity : AppCompatActivity() {

    private val TAG = "QuizActivity"

    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseDB: FirebaseDatabase
    private lateinit var quizRef: DatabaseReference


    //private var answers = ArrayList<String>()

    private lateinit var survey: SurveyView
    private lateinit var container: ViewGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummy_survey)
        supportActionBar?.hide()


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null).
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            startRegisterUser()
        }

        survey = findViewById(R.id.survey_view)
        container = findViewById(R.id.surveyContainer)
        setupSurvey(survey)
    }

    private fun startRegisterUser() {

        val intentRegister = Intent(this, LoginUser::class.java)
        startActivity(intentRegister)
        finish()
        // security related, we want the user to not have access to main activity
        // without login/sign up so main activity does not go to the call bak stack
    }

    private fun toMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun setupSurvey(surveyView: SurveyView) {

        val quizEntities = listOf<QuizEntity>(
            QuizEntity(
                QuestionStep(
                    title = this.resources.getString(R.string.quiz_question_title_1),
                    text = this.resources.getString(R.string.quiz_text_1),
                    answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                        textChoices = listOf(
                            TextChoice(this.resources.getString(R.string.question_answer_1_1)),
                            TextChoice(this.resources.getString(R.string.question_answer_1_2)),
                            TextChoice(this.resources.getString(R.string.question_answer_1_3)),
                            TextChoice(this.resources.getString(R.string.question_answer_1_4))
                        )
                    )
                ),
                "A lock icon to the left of the URL"
            ),
            QuizEntity(
                QuestionStep(
                    title = this.resources.getString(R.string.quiz_question_title_2),
                    text = this.resources.getString(R.string.quiz_text_2),
                    answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                        textChoices = listOf(
                            TextChoice(this.resources.getString(R.string.question_answer_2_1)),
                            TextChoice(this.resources.getString(R.string.question_answer_2_2)),
                            TextChoice(this.resources.getString(R.string.question_answer_2_3)),
                            TextChoice(this.resources.getString(R.string.question_answer_2_4))
                        )
                    )
                ),
                "A small piece of data that tracks your preferences on different websites"
            ),
            QuizEntity(
                QuestionStep(
                    title = this.resources.getString(R.string.quiz_question_title_3),
                    text = this.resources.getString(R.string.quiz_text_3),
                    answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                        textChoices = listOf(
                            TextChoice(this.resources.getString(R.string.question_answer_3_1)),
                            TextChoice(this.resources.getString(R.string.question_answer_3_2)),
                            TextChoice(this.resources.getString(R.string.question_answer_3_3)),
                            TextChoice(this.resources.getString(R.string.question_answer_3_4))
                        )
                    )
                ), "Credit cards"

            ),
            QuizEntity(
                QuestionStep(
                    title = this.resources.getString(R.string.quiz_question_title_4),
                    text = this.resources.getString(R.string.quiz_text_4),
                    answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                        textChoices = listOf(
                            TextChoice(this.resources.getString(R.string.question_answer_4_1)),
                            TextChoice(this.resources.getString(R.string.question_answer_4_2)),
                            TextChoice(this.resources.getString(R.string.question_answer_4_3)),
                            TextChoice(this.resources.getString(R.string.question_answer_4_4))
                        )
                    )
                ), "c^72j5pyR#%"

            ),
            QuizEntity(
                QuestionStep(
                    title = this.resources.getString(R.string.quiz_question_title_5),
                    text = this.resources.getString(R.string.quiz_text_5),
                    answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                        textChoices = listOf(
                            TextChoice(this.resources.getString(R.string.question_answer_5_1)),
                            TextChoice(this.resources.getString(R.string.question_answer_5_2)),
                            TextChoice(this.resources.getString(R.string.question_answer_5_3)),
                            TextChoice(this.resources.getString(R.string.question_answer_5_4))
                        )
                    )
                ), "Phishing scams"
            )
        )

        val instructionStep = InstructionStep(
            title = this.resources.getString(R.string.dummy_intro_title),
            text = this.resources.getString(R.string.dummy_intro_text),
            buttonText = this.resources.getString(R.string.dummy_intro_start)
        )

        val completionStep = CompletionStep(
            title = this.resources.getString(R.string.finish_question_title),
            text = this.resources.getString(R.string.finish_question_text),
            buttonText = this.resources.getString(R.string.finish_question_submit)
        )

        // building wrapper
        val stepsQuestions = arrayListOf<Step>()
        //add intro step
        stepsQuestions.add(instructionStep)
        for (quizEntity in quizEntities) {
            // add all question steps
            stepsQuestions.add(quizEntity.questionStep)
        }
        // add ending step
        stepsQuestions.add(completionStep)


        val steps = listOf(
            InstructionStep(
                title = this.resources.getString(R.string.dummy_intro_title),
                text = this.resources.getString(R.string.dummy_intro_text),
                buttonText = this.resources.getString(R.string.dummy_intro_start)
            ),
            QuestionStep(
                title = this.resources.getString(R.string.quiz_question_title_1),
                text = this.resources.getString(R.string.quiz_text_1),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.question_answer_1_1)),
                        TextChoice(this.resources.getString(R.string.question_answer_1_2)),
                        TextChoice(this.resources.getString(R.string.question_answer_1_3)),
                        TextChoice(this.resources.getString(R.string.question_answer_1_4))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.quiz_question_title_2),
                text = this.resources.getString(R.string.quiz_text_2),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.question_answer_2_1)),
                        TextChoice(this.resources.getString(R.string.question_answer_2_2)),
                        TextChoice(this.resources.getString(R.string.question_answer_2_3)),
                        TextChoice(this.resources.getString(R.string.question_answer_2_4))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.quiz_question_title_3),
                text = this.resources.getString(R.string.quiz_text_3),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.question_answer_3_1)),
                        TextChoice(this.resources.getString(R.string.question_answer_3_2)),
                        TextChoice(this.resources.getString(R.string.question_answer_3_3)),
                        TextChoice(this.resources.getString(R.string.question_answer_3_4))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.quiz_question_title_4),
                text = this.resources.getString(R.string.quiz_text_4),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.question_answer_4_1)),
                        TextChoice(this.resources.getString(R.string.question_answer_4_2)),
                        TextChoice(this.resources.getString(R.string.question_answer_4_3)),
                        TextChoice(this.resources.getString(R.string.question_answer_4_4))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.quiz_question_title_5),
                text = this.resources.getString(R.string.quiz_text_5),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.question_answer_5_1)),
                        TextChoice(this.resources.getString(R.string.question_answer_5_2)),
                        TextChoice(this.resources.getString(R.string.question_answer_5_3)),
                        TextChoice(this.resources.getString(R.string.question_answer_5_4))
                    )
                )
            ),
            CompletionStep(
                title = this.resources.getString(R.string.finish_question_title),
                text = this.resources.getString(R.string.finish_question_text),
                buttonText = this.resources.getString(R.string.finish_question_submit)
            )
        )

        val task = NavigableOrderedTask(steps = steps)
        //val task = NavigableOrderedTask(steps = stepsQuestions)

        task.setNavigationRule(
            steps[1].id,
            NavigationRule.DirectStepNavigationRule(
                destinationStepStepIdentifier = steps[2].id
            )
        )

        //task.setNavigationRule(
        //    stepsQuestions[1].id,
        //    NavigationRule.DirectStepNavigationRule(
        //        destinationStepStepIdentifier = stepsQuestions[2].id
        //    )
        //)


        // create questions array and populate
        val questionsArray = ArrayList<String>()
        questionsArray.add(getString(R.string.quiz_question_title_1))
        questionsArray.add(getString(R.string.quiz_question_title_2))
        questionsArray.add(getString(R.string.quiz_question_title_3))
        questionsArray.add(getString(R.string.quiz_question_title_4))
        questionsArray.add(getString(R.string.quiz_question_title_5))

        // create correct answers array and populate
        val correctAnswerArray = ArrayList<String>()
        correctAnswerArray.add(getString(R.string.question_answer_1_2))
        correctAnswerArray.add(getString(R.string.question_answer_2_2))
        correctAnswerArray.add(getString(R.string.question_answer_3_3))
        correctAnswerArray.add(getString(R.string.question_answer_4_1))
        correctAnswerArray.add(getString(R.string.question_answer_5_4))

        // create
        val userResponseArray = ArrayList<String>()
        val correctBoolArray = ArrayList<Boolean>()


        val quizResults = ArrayList<SingleResponse>()

        surveyView.onSurveyFinish = { taskResult: TaskResult, reason: FinishReason ->
            if (reason == FinishReason.Completed) {
                taskResult.results.forEachIndexed { index, stepResult ->

                    //Log.d(TAG, "answer ${stepResult.results.firstOrNull()}")
                    Log.e(TAG, "stepResult: ${stepResult.results.firstOrNull()}")

                    val value = stepResult.results.firstOrNull()

                    if (value?.stringIdentifier.toString().isNotEmpty()) {
                        val userAnswer =
                            stepResult.results.firstOrNull()?.stringIdentifier.toString()
                        //val questionTitle = quizEntities[index - 1].questionStep.title
                        val questionTitle = questionsArray[index-1]
                        val correctAnswer = quizEntities[index - 1].correctAnswer
                        val correctOrNot = correctAnswer == userAnswer

                        val quizResult =
                            SingleResponse(questionTitle, correctAnswer, userAnswer, correctOrNot)
                        quizResults.add(quizResult)
                    }

                }
                container.removeAllViews()
                //Log.d(TAG, "task result size: ${taskResult.results.size}")

            }

            Log.d(TAG, "array size: ${userResponseArray.size}")
            Log.d(TAG, "response 1 : ${userResponseArray[0]} +  ${correctBoolArray[0]}")
            Log.d(TAG, "response 2 : ${userResponseArray[1]} +  ${correctBoolArray[1]}")
            Log.d(TAG, "response 3 : ${userResponseArray[2]} +  ${correctBoolArray[2]}")
            Log.d(TAG, "response 4 : ${userResponseArray[3]} +  ${correctBoolArray[3]}")
            Log.d(TAG, "response 5 : ${userResponseArray[4]} +  ${correctBoolArray[4]}")

            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();

            // set up realtime database
            firebaseDB = FirebaseDatabase.getInstance()
            quizRef = firebaseDB.getReference("QuizData")

            // grab user uID
            val uID = mAuth.currentUser!!.uid

        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

        val configuration = SurveyTheme(
            themeColorDark = ContextCompat.getColor(this, R.color.cyan_normal),
            themeColor = ContextCompat.getColor(this, R.color.cyan_normal),
            textColor = ContextCompat.getColor(this, R.color.cyan_text)
        )

        surveyView.start(task, configuration)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            survey.backPressed()
            true
        } else false
    }
}


/**
//write data
quizRef.child(uID).setValue(quiz).addOnCompleteListener { task ->
if (task.isSuccessful) {
Log.d(TAG, "Quiz data added to DB user: $uID")
} else {
Log.e(TAG, "Quiz data write is not successful:${task.exception} ")
Toast.makeText(
this, "Quiz data write failed.",
Toast.LENGTH_SHORT
).show();
}
}
 */



