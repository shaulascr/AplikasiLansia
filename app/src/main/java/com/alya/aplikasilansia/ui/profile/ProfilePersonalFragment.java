package com.alya.aplikasilansia.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alya.aplikasilansia.LoginActivity;
import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.data.User;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class ProfilePersonalFragment extends Fragment {
    private static final String TAG = "ProfilePersonalFragment";

    private FirebaseAuth mAuth;
    private Button signOut;
    private ProfileViewModel profileViewModel;
    private TextView emailTextView, birthDateTextView, userNameTextView, ageTextView, genderTextView;

    public ProfilePersonalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_personal, container, false);
        emailTextView = view.findViewById(R.id.tv_email);
        birthDateTextView = view.findViewById(R.id.tv_date_profile);
        ageTextView = view.findViewById(R.id.tv_age_profile);
        genderTextView = view.findViewById(R.id.tv_gender);
        userNameTextView = view.findViewById(R.id.tv_username_profile);
        signOut = view.findViewById(R.id.btn_keluar);

        // Observe userLiveData from ViewModel
        profileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    emailTextView.setText(user.getEmail());
                    birthDateTextView.setText(user.getBirthDate());
                    userNameTextView.setText(user.getUserName());
                    genderTextView.setText(user.getGender());
                    setAge(user.getBirthDate());
                }
            }
        });

        signOut.setOnClickListener(v -> {
            profileViewModel.signOut();
            // Navigate to login screen or other appropriate action after sign out
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        // Fetch updated user data whenever the fragment is resumed
        profileViewModel.fetchUser();
    }

    private void setAge(String birthDate){
        if (birthDate != null && !birthDate.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Calendar birthDateCalendar = Calendar.getInstance();
                birthDateCalendar.setTime(Objects.requireNonNull(sdf.parse(birthDate)));
                Calendar today = Calendar.getInstance();
                int age = today.get(Calendar.YEAR) - birthDateCalendar.get(Calendar.YEAR);
                if (today.get(Calendar.DAY_OF_YEAR) < birthDateCalendar.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }
                String ageText = getString(R.string.age_format, age);
                ageTextView.setText(ageText);

                // Log age text
                Log.d(TAG, "Age: " + ageText);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e(TAG, "Error parsing date: " + e.getMessage());
            }
        } else {
            ageTextView.setText("N/A");
            Log.e(TAG, "Birthdate is empty or null");
        }
    }

}