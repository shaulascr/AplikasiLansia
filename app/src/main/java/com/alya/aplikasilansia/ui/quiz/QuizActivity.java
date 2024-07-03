package com.alya.aplikasilansia.ui.quiz;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.data.Question;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private QuizViewModel quizViewModel;
    private TextView questionText;
    private TextView scoreText;
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
//        scoreText = findViewById(R.id.scoreText);
        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        buttonPrevious = findViewById(R.id.buttonPrevious);
        buttonNext = findViewById(R.id.buttonNext);
        recyclerView = findViewById(R.id.recyclerView_quiz);

        // Setup RecyclerView with GridLayoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        // Observe LiveData from ViewModel and update UI accordingly
        quizViewModel.getQuestions().observe(this, questions -> {
            if (questions != null && !questions.isEmpty()) {
                displayQuestion(0);
                recyclerView.setAdapter(new QuestionAdapter(getQuestionNumbers(questions.size()), quizViewModel.getAnswers().getValue(), index -> quizViewModel.selectQuestion(index)));
            }
        });

        quizViewModel.getAnswers().observe(this, answers -> {
            if (answers != null) {
                recyclerView.setAdapter(new QuestionAdapter(getQuestionNumbers(quizViewModel.getTotalQuestions()), answers, index -> quizViewModel.selectQuestion(index)));
            }
        });

        quizViewModel.getCurrentQuestion().observe(this, this::displayQuestion);
        quizViewModel.getTotalScore().observe(this, score -> scoreText.setText("Score: " + score));

        // Setup button click listeners
        buttonYes.setOnClickListener(v -> quizViewModel.answerQuestion(true, getScoreForCurrentQuestion()));
        buttonNo.setOnClickListener(v -> quizViewModel.answerQuestion(false, 0));
        buttonPrevious.setOnClickListener(v -> quizViewModel.navigateToPrevious());
        buttonNext.setOnClickListener(v -> quizViewModel.navigateToNext());
    }

    private void displayQuestion(int index) {
        List<Question> questions = quizViewModel.getQuestions().getValue();
        if (questions != null && index >= 0 && index < questions.size()) {
            Question question = questions.get(index);
            questionText.setText(question.getText());
        } else {
            questionText.setText("No question available.");
        }
    }

    private List<Integer> getQuestionNumbers(int totalQuestions) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < totalQuestions; i++) {
            numbers.add(i + 1);
        }
        return numbers;
    }

    private int getScoreForCurrentQuestion() {
        int currentIndex = quizViewModel.getCurrentQuestion().getValue();
        List<Question> questions = quizViewModel.getQuestions().getValue();
        if (questions != null && currentIndex >= 0 && currentIndex < questions.size()) {
            return questions.get(currentIndex).getScore();
        }
        return 0;
    }
}


//    private void displayTotalScore() {
//        int totalScore = quizViewModel.calculateTotalScore();
//        Toast.makeText(this, "Total Score: " + totalScore, Toast.LENGTH_LONG).show();
//    }
//}


