package com.alya.aplikasilansia.ui.profile;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alya.aplikasilansia.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<User> userLiveData;
    private FirebaseAuth mAuth;

    public ProfileViewModel() {
        userLiveData = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
        fetchUser();
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    private void fetchUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            // Assuming you can get birthDate from Firebase or another source
            String email = firebaseUser.getEmail();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String birthDate = snapshot.child("birthDate").getValue(String.class);
                        String userName = snapshot.child("userName").getValue(String.class);
                        User userProfile = new User(email, birthDate, userName);
                        userLiveData.setValue(userProfile);
                    } else {
                        Log.d(TAG, "No such user data");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "Database error: ", error.toException());
                }
            });
        }
    }

    public void signOut() {
        mAuth.signOut();
    }

}
