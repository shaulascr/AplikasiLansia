package com.alya.aplikasilansia;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alya.aplikasilansia.data.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class RegisterViewModel extends ViewModel {
    private UserRepository userRepository;
    public MutableLiveData<FirebaseUser> userLiveData;
    public MutableLiveData<String> errorLiveData;

    public RegisterViewModel() {
        userRepository = new UserRepository();
        userLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public void register(String email, String password, String birthDate, String userName) {
        userRepository.register(email, password, birthDate, userName, userLiveData, errorLiveData);
    }
}

