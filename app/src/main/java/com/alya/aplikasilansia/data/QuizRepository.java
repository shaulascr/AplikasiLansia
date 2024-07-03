package com.alya.aplikasilansia.data;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizRepository {

    private final DatabaseReference questionsRef;

    public QuizRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        questionsRef = database.getReference("questions");
    }

    public MutableLiveData<List<Question>> getQuestions() {
        MutableLiveData<List<Question>> liveData = new MutableLiveData<>();

        questionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Question> questions = new ArrayList<>();
                for (DataSnapshot questionSnapshot : snapshot.getChildren()) {
                    Question question = questionSnapshot.getValue(Question.class);
                    questions.add(question);
                }
                liveData.setValue(questions);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database error
            }
        });

        return liveData;
    }
}


