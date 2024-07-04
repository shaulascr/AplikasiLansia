package com.alya.aplikasilansia.ui.quiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.data.Question;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class QuizActivity extends AppCompatActivity {

    private QuizViewModel quizViewModel;
    private TextView questionText;
    private Button buttonYes;
    private Button buttonNo;
    private Button buttonNext;
    private Button buttonPrevious;
    private Button buttonEndQuiz;
    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionText = findViewById(R.id.questionText);
        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        buttonNext = findViewById(R.id.buttonNext);
        buttonPrevious = findViewById(R.id.buttonPrevious);
        buttonEndQuiz = findViewById(R.id.buttonEndQuiz);
        recyclerView = findViewById(R.id.recyclerView_quiz);

        // Set layout manager for the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        quizViewModel.getQuestionsLiveData().observe(this, questions -> {
            if (questions != null && !questions.isEmpty()) {
                displayQuestion(questions.get(0));
                questionAdapter = new QuestionAdapter(questions, position -> quizViewModel.selectQuestion(position));
                recyclerView.setAdapter(questionAdapter);
            }
        });

        quizViewModel.getCurrentQuestion().observe(this, this::displayQuestion);

        quizViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null && !isLoading) {
                Snackbar.make(findViewById(R.id.activity_quiz), "Failed to load questions from Firebase", Snackbar.LENGTH_LONG).show();
            }
        });

        quizViewModel.getQuizCompleted().observe(this, quizCompleted -> {
            if (quizCompleted != null && quizCompleted) {
                Toast.makeText(this, "Quiz completed successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity or navigate to another screen
            }
        });

        quizViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Button click listeners
        buttonYes.setOnClickListener(v -> {
            quizViewModel.updateUserAnswer(1, true); // Update user's answer to true (Yes)
            moveToNextQuestion();
        });

        buttonNo.setOnClickListener(v -> {
            quizViewModel.updateUserAnswer(0, false); // Update user's answer to false (No)
            moveToNextQuestion();
        });

        buttonNext.setOnClickListener(v -> moveToNextQuestion());

        buttonPrevious.setOnClickListener(v -> moveToPreviousQuestion());

        buttonEndQuiz.setOnClickListener(v -> {
            // Collect user answers
            List<Question> answeredQuestions = quizViewModel.getQuestionsLiveData().getValue();
            if (answeredQuestions != null) {
                Map<String, Boolean> userAnswers = new HashMap<>();
                for (int i = 0; i < answeredQuestions.size(); i++) {
                    boolean userAnswer = getUserAnswerForQuestion(i);
                    userAnswers.put("question_" + i, userAnswer);
                }
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String quizId = generateQuizId(); // Replace with actual quiz ID generation logic
                quizViewModel.endQuiz(userId, quizId, userAnswers);
            }
        });
    }

    private void displayQuestion(Question question) {
        if (question != null) {
            questionText.setText(question.getText());
            int currentIndex = quizViewModel.getCurrentQuestionIndex();
            int totalQuestions = quizViewModel.getTotalQuestions();

            if (currentIndex == totalQuestions - 1) {
                buttonEndQuiz.setVisibility(View.VISIBLE);
                buttonNext.setVisibility(View.GONE);
            } else {
                buttonEndQuiz.setVisibility(View.GONE);
                buttonNext.setVisibility(View.VISIBLE);
            }
        }
    }

    private void moveToNextQuestion() {
        int nextIndex = quizViewModel.getCurrentQuestionIndex() + 1;
        if (nextIndex < quizViewModel.getTotalQuestions()) {
            quizViewModel.selectQuestion(nextIndex);
        }
    }

    private void moveToPreviousQuestion() {
        int prevIndex = quizViewModel.getCurrentQuestionIndex() - 1;
        if (prevIndex >= 0) {
            quizViewModel.selectQuestion(prevIndex);
        }
    }

    // Replace with actual quiz ID generation logic
    private String generateQuizId() {
        return UUID.randomUUID().toString(); // Generate a random UUID as quiz ID
    }

    private boolean getUserAnswerForQuestion(int questionIndex) {
        // Implement logic to retrieve the user's answer for the question at questionIndex
        // This could involve tracking the answers in the ViewModel or another approach
        // For now, let's return a dummy value
        return true; // Replace with actual answer logic
    }
}



//    private void displayTotalScore() {
//        int totalScore = quizViewModel.calculateTotalScore();
//        Toast.makeText(this, "Total Score: " + totalScore, Toast.LENGTH_LONG).show();
//    }
//}


