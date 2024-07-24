package com.alya.aplikasilansia.ui.bloodpressure;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alya.aplikasilansia.data.BloodPresRepository;
import com.alya.aplikasilansia.data.BloodPressure;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class BloodPresViewModel extends ViewModel {
    private BloodPresRepository repository;
    private MutableLiveData<List<BloodPressure>> bloodPressureData;
    private MutableLiveData<FirebaseUser> pressureLiveData;
    private MutableLiveData<String> errorLiveData;

    public BloodPresViewModel() {
        repository = new BloodPresRepository();
        bloodPressureData = repository.fetchingBloodPres();
        pressureLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public LiveData<List<BloodPressure>> getBloodPressureData() {
        if (bloodPressureData.getValue() == null) {
            bloodPressureData = repository.fetchingBloodPres();
        }
        return bloodPressureData;
    }
    public LiveData<FirebaseUser> getPressureLiveData() {
        return pressureLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void fetchBloodPressureData() {
        bloodPressureData = repository.fetchingBloodPres();
    }

    public void addBloodPressure(String bloodPressure, String pulse, String timestamp) {
        repository.addPressure(bloodPressure, pulse, timestamp, pressureLiveData, errorLiveData);
    }
}
