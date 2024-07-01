package com.alya.aplikasilansia.ui.quiz;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.R;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private QuizViewModel quizViewModel;

    private TextView questionText;
    private Button buttonYes;
    private Button buttonNo;
    private Button buttonPrevious;
    private Button buttonNext;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize the ViewModel
        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        // Initialize UI components
        questionText = findViewById(R.id.questionText);
        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        buttonPrevious = findViewById(R.id.buttonPrevious);
        buttonNext = findViewById(R.id.buttonNext);
        recyclerView = findViewById(R.id.recyclerView);

        // Setup RecyclerView with GridLayoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        // Observe LiveData from ViewModel and update UI accordingly
        quizViewModel.getQuestionText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String text) {
                questionText.setText(text);
            }
        });

        quizViewModel.getAnswers().observe(this, new Observer<Boolean[]>() {
            @Override
            public void onChanged(Boolean[] answers) {
                recyclerView.setAdapter(new QuestionAdapter(getQuestionNumbers(quizViewModel.getTotalQuestions()), answers, new QuestionAdapter.OnQuestionClickListener() {
                    @Override
                    public void onQuestionClick(int index) {
                        quizViewModel.selectQuestion(index);
                    }
                }));
            }
        });

        quizViewModel.getCurrentQuestion().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer index) {
                displayQuestion(index);
            }
        });

        // Setup button click listeners
        buttonYes.setOnClickListener(v -> quizViewModel.answerQuestion(true));
        buttonNo.setOnClickListener(v -> quizViewModel.answerQuestion(false));
        buttonPrevious.setOnClickListener(v -> quizViewModel.navigateToPrevious());
        buttonNext.setOnClickListener(v -> quizViewModel.navigateToNext());
    }

    private void displayQuestion(int index) {
        Boolean[] answers = quizViewModel.getAnswers().getValue();
        if (answers != null) {
            Boolean answer = answers[index];
            buttonYes.setEnabled(answer == null);
            buttonNo.setEnabled(answer == null);
        }
    }

    private List<Integer> getQuestionNumbers(int totalQuestions) {
        List<Integer> questionNumbers = new ArrayList<>();
        for (int i = 0; i < totalQuestions; i++) {
            questionNumbers.add(i + 1); // Adding 1 to start from 1 instead of 0
        }
        return questionNumbers;
    }
}

