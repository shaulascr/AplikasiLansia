package com.alya.aplikasilansia.ui.profile;

import android.content.Intent;
import android.os.Bundle;
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

public class ProfilePersonalFragment extends Fragment {

    private FirebaseAuth mAuth;
    private Button signOut;
    private ProfileViewModel profileViewModel;
    private TextView emailTextView, birthDateTextView, userNameTextView;

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

}