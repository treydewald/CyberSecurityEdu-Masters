package com.example.cybersecurityeducation

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier


class TensorFlowDatabaseAccess : AppCompatActivity() {
    private lateinit var firebaseDB: FirebaseDatabase
    private lateinit var surveyRef: DatabaseReference
    private lateinit var userRef: DatabaseReference
    private var context: Context? = null
    private val TAG = "TaskApi"
    private val MODEL_PATH = "text_classification.tflite"

    var classifier: NLClassifier? = null

    private var client: TextClassificationClient? = null

    private var resultTextView: TextView? = null
    private var inputEditText: EditText? = null
    private var handler: Handler? = null
    private var scrollView: ScrollView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tensor_flow_database_access)
        firebaseDB = FirebaseDatabase.getInstance()
        surveyRef = firebaseDB.getReference("SurveyData")
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        surveyRef.child(userID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val surveyData: SurveyData? = dataSnapshot.getValue(SurveyData::class.java)
                val answers = surveyData?.q1?.answer +" "+surveyData?.q2?.answer +" " +surveyData?.q3?.answer +" " +surveyData?.q4?.answer  +" "+surveyData?.q5?.answer +" " +surveyData?.q6?.answer +" " +surveyData?.q7?.answer +" " +surveyData?.q8?.answer +" " +surveyData?.q9?.answer +" " +surveyData?.q10?.answer +" " +surveyData?.q11?.answer +" " +surveyData?.q12?.answer +" " +surveyData?.q13?.answer +" " +surveyData?.q14?.answer +" " +surveyData?.q15?.answer +" " +surveyData?.q16?.answer +" " +surveyData?.q17?.answer +" " +surveyData?.q18?.answer +" " +surveyData?.q19?.answer +" " +surveyData?.q20?.answer
                System.out.println(answers)

            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                System.out.println("Failed")
            }
            // set title to user name
        })
    }
    override fun onStart() {
        super.onStart()
        System.out.println("onStart")
        handler!!.post { client!!.load() }
    }
    override fun onStop() {
        super.onStop()
        System.out.println("onStop")
        handler!!.post { client!!.unload() }
    }

    /** Send input text to TextClassificationClient and get the classify messages.  */
    private fun classify(text: String) {
        handler!!.post {

            // Run text classification with TF Lite.
            val results =
                client!!.classify(text)

            // Show classification result on screen
            showResult(text, results)
        }
    }
    private fun showResult(
        inputText: String,
        results: List<Result>
    ) {
        // Run on UI thread as we'll updating our app UI
        runOnUiThread {
            var textToShow = "Input: $inputText\nOutput:\n"
            for (i in results.indices) {
                val result = results[i]
                textToShow += String.format(
                    "    %s: %s\n",
                    result.title,
                    result.confidence
                )
            }
            textToShow += "---------\n"

            // Append the result to the UI.
            resultTextView!!.append(textToShow)

            // Clear the input text.
            inputEditText!!.text.clear()

            // Scroll to the bottom to show latest entry's classification result.
            scrollView!!.post { scrollView!!.fullScroll(View.FOCUS_DOWN) }
        }
    }
}