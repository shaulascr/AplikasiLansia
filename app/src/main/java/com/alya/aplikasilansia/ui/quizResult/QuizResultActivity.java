package com.alya.aplikasilansia.ui.quizResult;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alya.aplikasilansia.R;

public class QuizResultActivity extends AppCompatActivity {

    private ProgressBar statsProgressBar;
    private TextView numberScoreTextView;
    private TextView resultScoreTextView;
    private Button backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        // Initialize views
        statsProgressBar = findViewById(R.id.stats_progressbar);
        numberScoreTextView = findViewById(R.id.number_score);
        resultScoreTextView = findViewById(R.id.result_score);
        backButton = findViewById(R.id.btn_back_result);

        // Retrieve and display quiz result
        int totalScore = getIntent().getIntExtra("total_score", 0);
        int maxScore = 15;
        String classifiedScore = getIntent().getStringExtra("classified_score");
        String quizId = getIntent().getStringExtra("quizId");

        // Set score to ProgressBar and TextViews
        updateDoughnutChart(totalScore, maxScore);
//        numberScoreTextView.setText("Score: " + totalScore);
        resultScoreTextView.setText("Tingkat depresi Anda: \n " + classifiedScore);

        // Set back button listener
        backButton.setOnClickListener(v -> finish());
    }

    private void updateDoughnutChart(int totalScore, int maxScore) {
        // Calculate percentage
        int percentage = (int) ((totalScore / (float) maxScore) * 100);

        // Update ProgressBar
        statsProgressBar.setProgress(percentage);

        // Update score display
        numberScoreTextView.setText(percentage + "%");
    }
}