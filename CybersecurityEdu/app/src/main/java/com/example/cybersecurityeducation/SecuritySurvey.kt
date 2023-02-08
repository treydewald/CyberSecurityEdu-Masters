package com.example.cybersecurityeducation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.quickbirdstudios.surveykit.*
import com.quickbirdstudios.surveykit.result.TaskResult
import com.quickbirdstudios.surveykit.steps.CompletionStep
import com.quickbirdstudios.surveykit.steps.InstructionStep
import com.quickbirdstudios.surveykit.steps.QuestionStep
import com.quickbirdstudios.surveykit.survey.SurveyView

private val TAG = "SurveyTemplate"
private lateinit var mAuth: FirebaseAuth
private lateinit var firebaseDB: FirebaseDatabase
private lateinit var userRef: DatabaseReference
lateinit var tensorIntent: Intent

class SecuritySurvey : AppCompatActivity() {

    private lateinit var survey: SurveyView
    private lateinit var container: ViewGroup
    private lateinit var surveyRef: DatabaseReference
    private var answers = ArrayList<ArrayList<String?>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey_template)
        supportActionBar?.hide() // hide action bar

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

    private fun setupSurvey(surveyView: SurveyView) {
        val steps = listOf(
            InstructionStep(
                title = this.resources.getString(R.string.security_title),
                text = this.resources.getString(R.string.security_text),
                buttonText = this.resources.getString(R.string.security_start)
            ),
            QuestionStep(
                title = this.resources.getString(R.string.antivirus_question_title),
                text = this.resources.getString(R.string.SecurityBehaviorQ1),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.antivirus_answer_yes)),
                        TextChoice(this.resources.getString(R.string.antivirus_answer_no)),
                        TextChoice(this.resources.getString(R.string.antivirus_answer_not_sure))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.password_manager_question_title),
                text = this.resources.getString(R.string.SecurityBehaviorQ2),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.password_manager_answer_web_browser)),
                        TextChoice(this.resources.getString(R.string.password_manager_answer_cloud_based)),
                        TextChoice(this.resources.getString(R.string.password_manager_answer_desktop_offline)),
                        TextChoice(this.resources.getString(R.string.password_manager_answer_operating_system)),
                        TextChoice(this.resources.getString(R.string.password_manager_answer_not_using)),
                        TextChoice(this.resources.getString(R.string.password_manager_answer_not_sure)),
                        TextChoice(this.resources.getString(R.string.password_manager_answer_other))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.secure_screen_lock_question_title),
                text = this.resources.getString(R.string.SecurityBehaviorQ3),
                answerFormat = AnswerFormat.MultipleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.secure_screen_lock_pin)),
                        TextChoice(this.resources.getString(R.string.secure_screen_lock_pattern)),
                        TextChoice(this.resources.getString(R.string.secure_screen_lock_password)),
                        TextChoice(this.resources.getString(R.string.secure_screen_lock_fingerprint)),
                        TextChoice(this.resources.getString(R.string.secure_screen_lock_face_id)),
                        TextChoice(this.resources.getString(R.string.secure_screen_lock_not_currently_using)),
                        TextChoice(this.resources.getString(R.string.secure_screen_lock_not_sure)),
                        TextChoice(this.resources.getString(R.string.secure_screen_lock_other))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.two_factor_authentication_question_title),
                text = this.resources.getString(R.string.SecurityBehaviorQ4),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.two_factor_authentication_yes)),
                        TextChoice(this.resources.getString(R.string.two_factor_authentication_no)),
                        TextChoice(this.resources.getString(R.string.two_factor_authentication_not_sure))
                    )
                )
            ),
            InstructionStep(
                title = this.resources.getString(R.string.security_sebis_title),
                text = this.resources.getString(R.string.security_sebis_text),
                buttonText = this.resources.getString(R.string.security_sebis_start)
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ1),
                text = this.resources.getString(R.string.automatic_lock_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.automatic_lock_never)),
                        TextChoice(this.resources.getString(R.string.automatic_lock_rarely)),
                        TextChoice(this.resources.getString(R.string.automatic_lock_sometimes)),
                        TextChoice(this.resources.getString(R.string.automatic_lock_often)),
                        TextChoice(this.resources.getString(R.string.automatic_lock_always))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ2),
                text = this.resources.getString(R.string.laptop_tablet_password_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.laptop_tablet_password_never)),
                        TextChoice(this.resources.getString(R.string.laptop_tablet_password_rarely)),
                        TextChoice(this.resources.getString(R.string.laptop_tablet_password_sometimes)),
                        TextChoice(this.resources.getString(R.string.laptop_tablet_password_often)),
                        TextChoice(this.resources.getString(R.string.laptop_tablet_password_always))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ3),
                text = this.resources.getString(R.string.manual_lock_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.manual_lock_never)),
                        TextChoice(this.resources.getString(R.string.manual_lock_rarely)),
                        TextChoice(this.resources.getString(R.string.manual_lock_sometimes)),
                        TextChoice(this.resources.getString(R.string.manual_lock_often)),
                        TextChoice(this.resources.getString(R.string.manual_lock_always))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ4),
                text = this.resources.getString(R.string.mobile_use_pin_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.mobile_use_pin_never)),
                        TextChoice(this.resources.getString(R.string.mobile_use_pin_rarely)),
                        TextChoice(this.resources.getString(R.string.mobile_use_pin_sometimes)),
                        TextChoice(this.resources.getString(R.string.mobile_use_pin_often)),
                        TextChoice(this.resources.getString(R.string.mobile_use_pin_always))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ5),
                text = this.resources.getString(R.string.do_not_change_passwords_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.do_not_change_passwords_never)),
                        TextChoice(this.resources.getString(R.string.do_not_change_passwords_rarely)),
                        TextChoice(this.resources.getString(R.string.do_not_change_passwords_sometimes)),
                        TextChoice(this.resources.getString(R.string.do_not_change_passwords_often)),
                        TextChoice(this.resources.getString(R.string.do_not_change_passwords_always))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ6),
                text = this.resources.getString(R.string.different_passwords_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.different_passwords_never)),
                        TextChoice(this.resources.getString(R.string.different_passwords_rarely)),
                        TextChoice(this.resources.getString(R.string.different_passwords_sometimes)),
                        TextChoice(this.resources.getString(R.string.different_passwords_often)),
                        TextChoice(this.resources.getString(R.string.different_passwords_always))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ7),
                text = this.resources.getString(R.string.new_online_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.new_online_never)),
                        TextChoice(this.resources.getString(R.string.new_online_rarely)),
                        TextChoice(this.resources.getString(R.string.new_online_sometimes)),
                        TextChoice(this.resources.getString(R.string.new_online_often)),
                        TextChoice(this.resources.getString(R.string.new_online_always))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ8),
                text = this.resources.getString(R.string.special_characters_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.special_characters_never)),
                        TextChoice(this.resources.getString(R.string.special_characters_rarely)),
                        TextChoice(this.resources.getString(R.string.special_characters_sometimes)),
                        TextChoice(this.resources.getString(R.string.special_characters_often)),
                        TextChoice(this.resources.getString(R.string.special_characters_always))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ9),
                text = this.resources.getString(R.string.link_verifying_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.link_verifying_never)),
                        TextChoice(this.resources.getString(R.string.link_verifying_rarely)),
                        TextChoice(this.resources.getString(R.string.link_verifying_sometimes)),
                        TextChoice(this.resources.getString(R.string.link_verifying_often)),
                        TextChoice(this.resources.getString(R.string.link_verifying_always))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ10),
                text = this.resources.getString(R.string.website_feel_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.website_feel_never)),
                        TextChoice(this.resources.getString(R.string.website_feel_rarely)),
                        TextChoice(this.resources.getString(R.string.website_feel_sometimes)),
                        TextChoice(this.resources.getString(R.string.website_feel_often)),
                        TextChoice(this.resources.getString(R.string.website_feel_always))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ11),
                text = this.resources.getString(R.string.information_verifying_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.information_verifying_never)),
                        TextChoice(this.resources.getString(R.string.information_verifying_rarely)),
                        TextChoice(this.resources.getString(R.string.information_verifying_sometimes)),
                        TextChoice(this.resources.getString(R.string.information_verifying_often)),
                        TextChoice(this.resources.getString(R.string.information_verifying_always))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ12),
                text = this.resources.getString(R.string.mouseover_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.mouseover_never)),
                        TextChoice(this.resources.getString(R.string.mouseover_rarely)),
                        TextChoice(this.resources.getString(R.string.mouseover_sometimes)),
                        TextChoice(this.resources.getString(R.string.mouseover_often)),
                        TextChoice(this.resources.getString(R.string.mouseover_always))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ13),
                text = this.resources.getString(R.string.security_continue_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.security_continue_never)),
                        TextChoice(this.resources.getString(R.string.security_continue_rarely)),
                        TextChoice(this.resources.getString(R.string.security_continue_sometimes)),
                        TextChoice(this.resources.getString(R.string.security_continue_often)),
                        TextChoice(this.resources.getString(R.string.security_continue_always))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ14),
                text = this.resources.getString(R.string.update_prompt_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.update_prompt_never)),
                        TextChoice(this.resources.getString(R.string.update_prompt_rarely)),
                        TextChoice(this.resources.getString(R.string.update_prompt_sometimes)),
                        TextChoice(this.resources.getString(R.string.update_prompt_often)),
                        TextChoice(this.resources.getString(R.string.update_prompt_always))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ15),
                text = this.resources.getString(R.string.up_to_date_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.up_to_date_never)),
                        TextChoice(this.resources.getString(R.string.up_to_date_rarely)),
                        TextChoice(this.resources.getString(R.string.up_to_date_sometimes)),
                        TextChoice(this.resources.getString(R.string.up_to_date_often)),
                        TextChoice(this.resources.getString(R.string.up_to_date_always))
                    )
                )
            ),
            QuestionStep(
                title = this.resources.getString(R.string.SebisScaleQ16),
                text = this.resources.getString(R.string.anti_virus_verify_question_text),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.anti_virus_verify_never)),
                        TextChoice(this.resources.getString(R.string.anti_virus_verify_rarely)),
                        TextChoice(this.resources.getString(R.string.anti_virus_verify_sometimes)),
                        TextChoice(this.resources.getString(R.string.anti_virus_verify_often)),
                        TextChoice(this.resources.getString(R.string.anti_virus_verify_always))
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

        task.setNavigationRule(
            steps[1].id,
            NavigationRule.DirectStepNavigationRule(
                destinationStepStepIdentifier = steps[2].id
            )
        )

        task.setNavigationRule(
            steps[7].id,
            NavigationRule.ConditionalDirectionStepNavigationRule(
                resultToStepIdentifierMapper = { input ->
                    when (input) {
                        "Ja" -> steps[7].id
                        "Nein" -> steps[0].id
                        else -> null
                    }
                }
            )
        )

        surveyView.onSurveyFinish = { taskResult: TaskResult, reason: FinishReason ->


            if (reason == FinishReason.Completed) {
                taskResult.results.forEach { stepResult ->
                    Log.d(TAG, "answer: ${stepResult.results.firstOrNull()?.stringIdentifier}")
                    val value = ArrayList<String?>()
                    value.add(stepResult.results.firstOrNull()?.stringIdentifier)
                    answers.add(value)
                    container.removeAllViews()
                }
                // clean up array before adding to DB
                answers.removeAt(0)
                answers.removeAt(answers.size-1)
                val prompt = 4 //
                answers.removeAt(prompt)
                Log.d(TAG, "--->answers array : $answers")
                Log.d(TAG, "------->answers array length : ${answers.size}")
                //Log.d(TAG, "------->answers array length : ${answers[0]}")
                //Log.d(TAG, "------->answers array length : ${answers[0][0]}")
                //Log.d(TAG, "------->answers array length : ${answers[3][0]?.length}")

                // Add questions and answers to data class

                val question1 = getString(R.string.SecurityBehaviorQ1)
                val question2 = getString(R.string.SecurityBehaviorQ2)
                val question3 = getString(R.string.SecurityBehaviorQ3)
                val question4 = getString(R.string.SecurityBehaviorQ4)

                val question5 = getString(R.string.SebisScaleQ1)
                val question6 = getString(R.string.SebisScaleQ2)
                val question7 = getString(R.string.SebisScaleQ3)
                val question8 = getString(R.string.SebisScaleQ4)
                val question9 = getString(R.string.SebisScaleQ5)
                val question10 = getString(R.string.SebisScaleQ6)

                val question11 = getString(R.string.SebisScaleQ7)
                val question12 = getString(R.string.SebisScaleQ8)
                val question13 = getString(R.string.SebisScaleQ9)
                val question14 = getString(R.string.SebisScaleQ10)
                val question15 = getString(R.string.SebisScaleQ11)
                val question16 = getString(R.string.SebisScaleQ12)

                val question17 = getString(R.string.SebisScaleQ13)
                val question18 = getString(R.string.SebisScaleQ14)
                val question19 = getString(R.string.SebisScaleQ15)
                val question20 = getString(R.string.SebisScaleQ16)

                val surveyDATA = SurveyData(
                    singleQuestion(question1,answers[0][0].toString()),
                    singleQuestion(question2,answers[1][0].toString()),
                    multipleQuestion(question3,answers[2]),
                    singleQuestion(question4,answers[3][0].toString()),
                    singleQuestion(question5,answers[4][0].toString()),
                    singleQuestion(question6,answers[5][0].toString()),
                    singleQuestion(question7,answers[6][0].toString()),
                    singleQuestion(question8,answers[7][0].toString()),
                    singleQuestion(question9,answers[8][0].toString()),
                    singleQuestion(question10,answers[9][0].toString()),
                    singleQuestion(question11,answers[10][0].toString()),
                    singleQuestion(question12,answers[11][0].toString()),
                    singleQuestion(question13,answers[12][0].toString()),
                    singleQuestion(question14,answers[13][0].toString()),
                    singleQuestion(question15,answers[14][0].toString()),
                    singleQuestion(question16,answers[15][0].toString()),
                    singleQuestion(question17,answers[16][0].toString()),
                    singleQuestion(question18,answers[17][0].toString()),
                    singleQuestion(question19,answers[18][0].toString()),
                    singleQuestion(question20,answers[19][0].toString())
                )

                Log.d(TAG, "survey : ${surveyDATA?.q3?.answer}")

                // Initialize Firebase Auth
                mAuth = FirebaseAuth.getInstance();

                // set up realtime database
                firebaseDB = FirebaseDatabase.getInstance()
                userRef = firebaseDB.getReference("SurveyData")

                //val userID = FirebaseAuth.getInstance().currentUser!!.uid
                val uID = mAuth.currentUser!!.uid

                userRef.child(uID).setValue(surveyDATA).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Survey data added to DB user: $uID")
                    } else {
                        Log.e(TAG, "Survey data write is not successful:${task.exception} ")
                        Toast.makeText(
                            this, "Survey data write failed.",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }

            }

            toTensorFlow()
            finish()
        }

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
    fun toMainActivity(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    fun toTensorFlow() {
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
}