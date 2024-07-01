package com.alya.aplikasilansia;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alya.aplikasilansia.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public MutableLiveData<FirebaseUser> userLiveData;
    public MutableLiveData<String> errorLiveData;

    public RegisterViewModel() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public void register(String email, String password, String birthDate, String userName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            User additionalUserInfo = new User(email, birthDate, userName);
                            mDatabase.child("users").child(user.getUid()).setValue(additionalUserInfo);
                            userLiveData.postValue(user);
                        }
                    } else {
                        errorLiveData.postValue(task.getException().getMessage());
                    }
                });
    }
}
