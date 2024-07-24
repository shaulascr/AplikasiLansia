package com.alya.aplikasilansia.data;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BloodPresRepository {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public BloodPresRepository() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    public MutableLiveData<List<BloodPressure>> fetchingBloodPres() {
        MutableLiveData<List<BloodPressure>> pressureLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            DatabaseReference pressureRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("bloodPressure");

            pressureRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<BloodPressure> bPressure = new ArrayList<>();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    Date now = new Date();

                    for (DataSnapshot pressureSnapshot : snapshot.getChildren()) {
                        String pres = pressureSnapshot.child("pressure").getValue(String.class);
                        String pulse = pressureSnapshot.child("pulse").getValue(String.class);
                        String date = pressureSnapshot.child("date").getValue(String.class);

                        if (pres != null && pulse != null && date != null && !date.isEmpty()) {
                            BloodPressure pressure = new BloodPressure(pres, pulse, date);
                            bPressure.add(pressure);
                        }
                    }
                    // Sort the list by date
                    Collections.sort(bPressure, new Comparator<BloodPressure>() {
                        @Override
                        public int compare(BloodPressure o1, BloodPressure o2) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            try {
                                Date date1 = dateFormat.parse(o1.getBpDate());
                                Date date2 = dateFormat.parse(o2.getBpDate());
                                if (date1 != null && date2 != null) {
                                    return date2.compareTo(date1); // Latest first
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    });
                    bPressure.removeIf(bpressure -> {
                        try {
                            Date pressureDate = sdf.parse(bpressure.getBpDate());
                            return pressureDate.before(now);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return false;
                        }
                    });


                    pressureLiveData.setValue(bPressure); // Set LiveData value here
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("ReminderRepository", "Database error: ", error.toException());
                    pressureLiveData.setValue(null);
                }
            });
        }
        return pressureLiveData;
    }

    public void addPressure(String bloodPressure, String pulse, String timestamp, MutableLiveData<FirebaseUser> pressureLiveData, MutableLiveData<String> errorLiveData) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            Log.d(TAG, "User ID: " + userId); // Log the user ID to verify it's correct
            DatabaseReference pressureRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("bloodPressure").push();

            Map<String, Object> data = new HashMap<>();
            data.put("pressure", bloodPressure);
            data.put("pulse", pulse);
            data.put("date", timestamp);
            pressureRef.setValue(data)
                    .addOnSuccessListener(aVoid -> {
                        // Handle success
                        Log.d(TAG, "Blood Pressure added successfully");
                        pressureLiveData.postValue(firebaseUser); // Update the live data with the user
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Log.e(TAG, "Failed to add Blood Pressure: " + e.getMessage());
                        errorLiveData.postValue("Failed to add Blood Pressure: " + e.getMessage());
                    });
        } else {
            errorLiveData.postValue("User not authenticated");
        }
    }
}
