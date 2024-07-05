package com.alya.aplikasilansia.ui.quiz;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.alya.aplikasilansia.data.Question;
import com.alya.aplikasilansia.data.QuizRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class QuizViewModel extends ViewModel {

    private final QuizRepository repository;
    private final LiveData<List<Question>> questionsLiveData;
    private final MutableLiveData<Integer> currentQuestionIndex = new MutableLiveData<>(0);
    private final LiveData<Question> currentQuestion;
    private final LiveData<Boolean> isLoading;
    private final MutableLiveData<Integer> score = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> quizCompleted = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Boolean>> userAnswers = new MutableLiveData<>(new ArrayList<>());


    public QuizViewModel() {
        repository = new QuizRepository();
        questionsLiveData = repository.getQuestionsLiveData();
        isLoading = repository.getIsLoading();
        currentQuestion = Transformations.switchMap(currentQuestionIndex, index -> {
            List<Question> questions = questionsLiveData.getValue();
            if (questions != null && index >= 0 && index < questions.size()) {
                return new MutableLiveData<>(questions.get(index));
            } else {
                return new MutableLiveData<>(null);
            }
        });
        // Initialize user answers list with false values
        questionsLiveData.observeForever(questions -> {
            if (questions != null) {
                List<Boolean> initialAnswers = new ArrayList<>(Collections.nCopies(questions.size(), false));
                userAnswers.setValue(initialAnswers);
            }
        });
    }
    public Integer calculateTotalScore() {
        int totalScore = 0;
        List<Boolean> answers = userAnswers.getValue();
        List<Question> questions = questionsLiveData.getValue();
        if (answers != null && questions != null) {
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                Boolean userAnswer = answers.get(i);
                if (userAnswer != null && userAnswer.equals(question.isCorrectAnswer())) {
                    totalScore++;
                }
            }
        }
        Log.e("QuizViewModel", "Null values or mismatched sizes in calculateTotalScore");

        return totalScore;
    }

    public LiveData<List<Question>> getQuestionsLiveData() {
        return questionsLiveData;
    }

    public LiveData<Question> getCurrentQuestion() {
        return currentQuestion;
    }
    public LiveData<List<Boolean>> getUserAnswers() {
        // Return LiveData<List<Boolean>> representing user answers
        return userAnswers;
    }


    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getQuizCompleted() {
        return quizCompleted;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void selectQuestion(int index) {
        currentQuestionIndex.setValue(index);
    }

    public void answerCurrentQuestion(boolean answer) {
        Integer index = currentQuestionIndex.getValue();
        if (index != null) {
            List<Boolean> currentAnswers = userAnswers.getValue();
            if (currentAnswers != null) {
                currentAnswers.set(index, answer);
                userAnswers.setValue(currentAnswers);
            }
        }
    }


    // Method to update user's answer for the current question
    public void updateUserAnswer(int index, boolean answer) {
        List<Boolean> answers = userAnswers.getValue();  // Get current list of answers
        if (answers != null && index >= 0 && index < answers.size()) {
            answers.set(index, answer);  // Update the answer at index
            userAnswers.setValue(answers);  // Update LiveData with new list
        }
    }


    public LiveData<Integer> getTotalScore() {
        return score;
    }

    public int getTotalQuestions() {
        List<Question> questions = questionsLiveData.getValue();
        return (questions == null) ? 0 : questions.size();
    }

    public int getCurrentQuestionIndex() {
        Integer index = currentQuestionIndex.getValue();
        return (index == null) ? 0 : index;
    }

    public void endQuiz(String userId, String quizId, Map<String, Boolean> userAnswers) {
        int finalScore = calculateTotalScore();

        Log.d("QuizViewModel", "Final score calculated: " + finalScore);

        repository.storeAnswers(userId, quizId, userAnswers, finalScore, new QuizRepository.OnStoreAnswersCompleteListener() {
            @Override
            public void onSuccess() {
                quizCompleted.setValue(true); // Update quiz completion status
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue(error); // Handle error if storing answers fails
            }
        });
    }
}





