package com.alya.aplikasilansia.ui.quiz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alya.aplikasilansia.data.Question;
import com.alya.aplikasilansia.data.QuizRepository;

import java.util.List;

public class QuizViewModel extends ViewModel {

    private final QuizRepository quizRepository;
    private final LiveData<List<Question>> questions;
    private final MutableLiveData<Boolean[]> answers;
    private final MutableLiveData<Integer> currentQuestion;
    private final MutableLiveData<Integer> totalScore;

    public QuizViewModel() {
        quizRepository = new QuizRepository();
        questions = quizRepository.getQuestions();
        answers = new MutableLiveData<>(new Boolean[getTotalQuestions()]);
        currentQuestion = new MutableLiveData<>(0);
        totalScore = new MutableLiveData<>(0);
    }

    public LiveData<List<Question>> getQuestions() {
        return questions;
    }

    public LiveData<Boolean[]> getAnswers() {
        return answers;
    }

    public LiveData<Integer> getCurrentQuestion() {
        return currentQuestion;
    }

    public LiveData<Integer> getTotalScore() {
        return totalScore;
    }

    public int getTotalQuestions() {
        return questions.getValue() != null ? questions.getValue().size() : 0;
    }

    public void selectQuestion(int index) {
        currentQuestion.setValue(index);
    }

    public void answerQuestion(boolean isCorrect, int score) {
        int currentIndex = currentQuestion.getValue();
        Boolean[] currentAnswers = answers.getValue();
        if (currentAnswers != null && currentAnswers[currentIndex] == null) {
            currentAnswers[currentIndex] = isCorrect;
            answers.setValue(currentAnswers);

            int currentScore = totalScore.getValue();
            if (isCorrect) {
                totalScore.setValue(currentScore + score);
            }
        }
    }

    public void navigateToPrevious() {
        int currentIndex = currentQuestion.getValue();
        if (currentIndex > 0) {
            currentQuestion.setValue(currentIndex - 1);
        }
    }

    public void navigateToNext() {
        int currentIndex = currentQuestion.getValue();
        if (currentIndex < getTotalQuestions() - 1) {
            currentQuestion.setValue(currentIndex + 1);
        }
    }
}






