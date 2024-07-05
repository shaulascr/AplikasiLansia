package com.alya.aplikasilansia.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizRepository {

    private final DatabaseReference databaseReference;
    private final MutableLiveData<List<Question>> questionsLiveData;
    private final MutableLiveData<Boolean> isLoading;

    public QuizRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("questions");
        questionsLiveData = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        fetchQuestions();
    }

    private void fetchQuestions() {
        isLoading.setValue(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Question> questions = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Question question = snapshot.getValue(Question.class);
                    questions.add(question);
                }
                questionsLiveData.setValue(questions);
                isLoading.setValue(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                isLoading.setValue(false);
                // Log the error or handle it as needed
            }
        });
    }

    public LiveData<List<Question>> getQuestionsLiveData() {
        return questionsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void storeAnswers(String userId, String quizId, Map<String, Boolean> userAnswers, int score, OnStoreAnswersCompleteListener listener) {
        DatabaseReference answersRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("quizzes").child(quizId);
        Map<String, Object> data = new HashMap<>();
        data.put("answers", userAnswers);
        data.put("score", score);

        Log.d("QuizRepository", "Storing answers for quizId: " + quizId + ", userId: " + userId + ", answers: " + userAnswers.toString() + ", score: " + score);


        answersRef.setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onSuccess();
            } else {
                listener.onFailure("Failed to submit answers. Please try again.");
            }
        });
    }

    public interface OnStoreAnswersCompleteListener {
        void onSuccess();
        void onFailure(String error);
    }
}


