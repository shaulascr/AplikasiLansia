package com.alya.aplikasilansia.ui.quiz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QuizViewModel extends ViewModel {

    private static final int TOTAL_QUESTIONS = 10; // Example: 10 questions
    private MutableLiveData<Integer> currentQuestion = new MutableLiveData<>(0);
    private MutableLiveData<Boolean[]> answers = new MutableLiveData<>(new Boolean[TOTAL_QUESTIONS]);
    private MutableLiveData<String> questionText = new MutableLiveData<>();
    private String[] questionList = {
            "What is the capital of France?",
            "Who painted the Mona Lisa?",
            "In which year did World War II end?",
            "What is the largest planet in our solar system?",
            "Who wrote 'Pride and Prejudice'?",
            "What is the chemical symbol for water?",
            "Who discovered penicillin?",
            "Which country is famous for kangaroos?",
            "Who invented the telephone?",
            "Which continent is the largest by land area?"
    };

    public QuizViewModel() {
        updateQuestionText();
    }

    public LiveData<Integer> getCurrentQuestion() {
        return currentQuestion;
    }

    public LiveData<Boolean[]> getAnswers() {
        return answers;
    }

    public LiveData<String> getQuestionText() {
        return questionText;
    }

    public void selectQuestion(int index) {
        currentQuestion.setValue(index);
        updateQuestionText();
    }

    public void answerQuestion(boolean answer) {
        Boolean[] answersArray = answers.getValue();
        if (answersArray != null) {
            answersArray[currentQuestion.getValue()] = answer;
            answers.setValue(answersArray);
        }
    }

    public void navigateToPrevious() {
        Integer currentIndex = currentQuestion.getValue();
        if (currentIndex != null && currentIndex > 0) {
            currentQuestion.setValue(currentIndex - 1);
            updateQuestionText();
        }
    }

    public void navigateToNext() {
        Integer currentIndex = currentQuestion.getValue();
        if (currentIndex != null && currentIndex < TOTAL_QUESTIONS - 1) {
            currentQuestion.setValue(currentIndex + 1);
            updateQuestionText();
        }
    }

    private void updateQuestionText() {
        Integer currentIndex = currentQuestion.getValue();
        if (currentIndex != null) {
            questionText.setValue(questionList[currentIndex]);
        }
    }

    public int getTotalQuestions() {
        return TOTAL_QUESTIONS;
    }
}


