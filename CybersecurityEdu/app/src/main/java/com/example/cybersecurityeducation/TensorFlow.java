package com.example.cybersecurityeducation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.List;
import com.example.cybersecurityeducation.Result;
import com.example.cybersecurityeducation.TextClassificationClient;

/** The main activity to provide interactions with users. */
public class TensorFlow extends AppCompatActivity {
    private static final String TAG = "TextClassificationDemo";

    private TextClassificationClient client;

    private TextView resultTextView;
    private EditText inputEditText;
    private Handler handler;
    private ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tensor_flow);
        Log.v(TAG, "onCreate");
        String answers = getIntent().getStringExtra("answers");
        Log.v(TAG, answers);
        client = new TextClassificationClient(getApplicationContext());
        handler = new Handler();
        Intent intent;
        Button classifyButton = findViewById(R.id.button);
        classifyButton.setOnClickListener(
                (View v) -> {
                    classify(answers);
                });
        final Button homeButton = (Button) findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(TensorFlow.this, MainActivity.class));
            }
        });
        resultTextView = findViewById(R.id.result_text_view);
        inputEditText = findViewById(R.id.input_text);
        scrollView = findViewById(R.id.scroll_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        handler.post(
                () -> {
                    client.load();
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        handler.post(
                () -> {
                    client.unload();
                });
    }

    /** Send input text to TextClassificationClient and get the classify messages. */
    private void classify(final String text) {
        handler.post(
                () -> {
                    // Run text classification with TF Lite.
                    List<Result> results = client.classify(text);

                    // Show classification result on screen
                    showResult(text, results);
                });
    }

    /** Show classification result on the screen. */
    private void showResult(final String inputText, final List<Result> results) {
        // Run on UI thread as we'll updating our app UI
        runOnUiThread(
                () -> {
                    String textToShow = "Input: " + inputText + "\nOutput:\n";
                    for (int i = 0; i < results.size(); i++) {
                        Result result = results.get(i);
                        textToShow += String.format("    %s: %s\n", result.getTitle(), result.getConfidence());
                    }
                    textToShow += "---------\n";

                    // Append the result to the UI.
                    resultTextView.append(textToShow);

                    // Clear the input text.
//                    inputEditText.getText().clear();

                    // Scroll to the bottom to show latest entry's classification result.
                    scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
                });
    }
}
