package com.alya.aplikasilansia;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    public MutableLiveData<FirebaseUser> userLiveData;
    public MutableLiveData<String> errorLiveData;

    public LoginViewModel() {
        mAuth = FirebaseAuth.getInstance();
        userLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        userLiveData.postValue(user);
                    } else {
                        errorLiveData.postValue(task.getException().getMessage());
                    }
                });
    }
}

