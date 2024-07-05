package com.alya.aplikasilansia.ui.reminder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alya.aplikasilansia.data.Reminder;
import com.alya.aplikasilansia.data.ReminderRepository;

import java.util.List;

public class ReminderViewModel extends ViewModel {
    private ReminderRepository reminderRepository;
    public MutableLiveData<List<Reminder>> reminderLiveData;
    public MutableLiveData<String> updateResultLiveData;

    public ReminderViewModel() {
        reminderLiveData = new MutableLiveData<>();
        updateResultLiveData = new MutableLiveData<>();
        reminderRepository = new ReminderRepository();
//        fetchReminder();
        fetchReminders();
    }

    public LiveData<List<Reminder>> getReminderLiveData(){
        return reminderLiveData;
    }
    public LiveData<String> getUpdateResultLiveData(){
        return updateResultLiveData;
    }
//    private void fetchReminder(){
//        reminderLiveData = reminderRepository.fetchReminder();
//    }
    private void fetchReminders(){
        reminderLiveData = reminderRepository.fetchReminder();
//        reminderLiveData.setValue(reminders);
    }
}
