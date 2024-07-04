package com.alya.aplikasilansia.ui.reminder;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alya.aplikasilansia.data.ReminderRepository;
import com.google.firebase.auth.FirebaseUser;

public class ReminderViewModel extends ViewModel {
    private ReminderRepository reminderRepository;
    public MutableLiveData<FirebaseUser> reminderLiveData;
    public MutableLiveData<String> updateResultLiveData;

}
